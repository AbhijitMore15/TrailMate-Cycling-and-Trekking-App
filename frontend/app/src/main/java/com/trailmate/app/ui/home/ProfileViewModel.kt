package com.trailmate.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.trailmate.app.api.ApiClient
import com.trailmate.app.utils.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val dataStore = DataStoreManager(app)

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {

            val token = dataStore.getAuthTokenOnce()

            if (token != null) {
                try {
                    val user = ApiClient.userApi.getCurrentUser("Bearer $token")
                    _name.value = user.name ?: ""
                } catch (e: Exception) {
                    _name.value = ""
                }
            }
        }
    }
}
