package com.trailmate.app.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.trailmate.app.api.ApiClient
import com.trailmate.app.models.RegisterRequest
import com.trailmate.app.models.RegisterResponse
import com.trailmate.app.models.ErrorResponse
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

                // ---------- REQUEST ----------
                val request = RegisterRequest(
                    name = name,
                    email = email,
                    password = password,
                    fitnessLevel = fitnessLevel
                )

                Log.d("REGISTER", "Request = $request")

                // ---------- API ----------
                val response = ApiClient.authApi.register(request)

                Log.d("REGISTER", "Code = ${response.code()}")

                // ---------- SUCCESS ----------
                if (response.isSuccessful && response.body() != null) {

                    val body: RegisterResponse = response.body()!!

                    _registerState.value = RegisterState.Success(
                        token = body.token,
                        userId = body.userId
                    )
                }

                // ---------- ERROR ----------
                else {

                    val raw = response.errorBody()?.string()

                    val message = try {
                        Gson().fromJson(raw, ErrorResponse::class.java).detail
                    } catch (_: Exception) {
                        "Registration failed"
                    }

                    _registerState.value = RegisterState.Error(message)
                }

            } catch (e: Exception) {

                Log.e("REGISTER", "Exception = ${e.message}")

                _registerState.value =
                    RegisterState.Error(e.message ?: "Unexpected error")
            }
        }
    }
}
