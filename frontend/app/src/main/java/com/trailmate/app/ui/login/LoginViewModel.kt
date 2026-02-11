package com.trailmate.app.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trailmate.app.api.ApiClient
import com.trailmate.app.models.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

                // Validate input
                if (!isValidEmail(email)) {
                    _loginState.value = LoginState.Error("Invalid email format")
                    Log.e("LoginVM", "Invalid email format")
                    return@launch
                }

                if (password.length < 6) {
                    _loginState.value = LoginState.Error("Password must be at least 6 characters")
                    Log.e("LoginVM", "Password too short")
                    return@launch
                }

                // Call backend API
                val loginRequest = LoginRequest(email = email, password = password)
                Log.d("LoginVM", "Making API call...")
                val response = ApiClient.authApi.login(loginRequest)

                Log.d("LoginVM", "Response code: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!
                    val token = responseBody.token
                    val userId = responseBody.userId
                    Log.d("LoginVM", "Login successful! Token received")
                    _loginState.value = LoginState.Success(token, userId)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Login failed"
                    Log.e("LoginVM", "Login failed: $errorMessage")
                    _loginState.value = LoginState.Error(errorMessage)
                }

            } catch (e: Exception) {
                val errorMsg = e.message ?: "An error occurred"
                Log.e("LoginVM", "Exception: $errorMsg", e)
                _loginState.value = LoginState.Error(errorMsg)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
}