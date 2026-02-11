package com.trailmate.app.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trailmate.app.api.ApiClient
import com.trailmate.app.models.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

                val response = ApiClient.authApi.register(request)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    _registerState.value =
                        RegisterState.Success(body.token, body.userId)
                } else {
                    _registerState.value =
                        RegisterState.Error(response.errorBody()?.string() ?: "Registration failed")
                }

            } catch (e: Exception) {
                _registerState.value =
                    RegisterState.Error(e.message ?: "Unexpected error")
            }
        }
    }
}
