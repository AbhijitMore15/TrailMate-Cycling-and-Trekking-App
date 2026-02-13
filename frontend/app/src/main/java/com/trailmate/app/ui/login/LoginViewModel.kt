package com.trailmate.app.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.trailmate.app.api.ApiClient
import com.trailmate.app.models.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ⭐ Error Model
import com.trailmate.app.models.ErrorResponse

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String, val userId: Int) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {

        viewModelScope.launch {

            try {
                _loginState.value = LoginState.Loading
                Log.d("LoginVM", "Login attempt: $email")

                // Input Validation
                if (!isValidEmail(email)) {
                    _loginState.value = LoginState.Error("Invalid email format")
                    return@launch
                }

                if (password.length < 6) {
                    _loginState.value = LoginState.Error("Password must be at least 6 characters")
                    return@launch
                }

                // API CALL
                val loginRequest = LoginRequest(email = email, password = password)
                Log.d("LoginVM", "Making API call...")

                val response = ApiClient.authApi.login(loginRequest)

                Log.d("LoginVM", "Response code: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {

                    val body = response.body()!!
                    Log.d("LoginVM", "Login successful")

                    _loginState.value = LoginState.Success(
                        token = body.token,
                        userId = body.userId
                    )

                } else {

                    val rawError = response.errorBody()?.string()

                    Log.e("LoginVM", "Raw error: $rawError")

                    val cleanMessage = try {
                        val parsed = Gson().fromJson(rawError, ErrorResponse::class.java)
                        parsed.detail
                    } catch (e: Exception) {
                        "Login failed"
                    }

                    _loginState.value = LoginState.Error(cleanMessage)
                }

            } catch (e: Exception) {

                Log.e("LoginVM", "Exception: ${e.message}", e)

                _loginState.value = LoginState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
}
