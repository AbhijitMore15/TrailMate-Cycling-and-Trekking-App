package com.trailmate.app.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.trailmate.app.api.ApiClient
import com.trailmate.app.models.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ⭐ SAME Error Model (Can reuse from Login if you move to common file)
import com.trailmate.app.models.ErrorResponse

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val token: String, val userId: Int) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(
        name: String,
        email: String,
        password: String,
        fitnessLevel: String
    ) {

        viewModelScope.launch {

            try {

                _registerState.value = RegisterState.Loading

                val request = RegisterRequest(
                    email = email,
                    password = password,
                    name = name,
                    fitnessLevel = fitnessLevel
                )

                Log.d("RegisterVM", "Making register API call")

                val response = ApiClient.authApi.register(request)

                Log.d("RegisterVM", "Response code: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {

                    val body = response.body()!!

                    Log.d("RegisterVM", "Registration successful")

                    _registerState.value = RegisterState.Success(
                        token = body.token,
                        userId = body.userId
                    )

                } else {

                    val rawError = response.errorBody()?.string()

                    Log.e("RegisterVM", "Raw error: $rawError")

                    val cleanMessage = try {
                        val parsed = Gson().fromJson(rawError, ErrorResponse::class.java)
                        parsed.detail
                    } catch (e: Exception) {
                        "Registration failed"
                    }

                    _registerState.value = RegisterState.Error(cleanMessage)
                }

            } catch (e: Exception) {

                Log.e("RegisterVM", "Exception: ${e.message}", e)

                _registerState.value = RegisterState.Error(
                    e.message ?: "Unexpected error"
                )
            }
        }
    }
}
