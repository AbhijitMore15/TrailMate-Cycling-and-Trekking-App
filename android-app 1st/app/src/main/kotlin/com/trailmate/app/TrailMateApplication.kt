package com.trailmate.app

import android.app.Application
import android.util.Log
import com.mappls.sdk.maps.Mappls

class TrailMateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Read Mappls API key from string resources if available
        val apiKey = run {
            try {
                val resId = resources.getIdentifier("mappls_api_key", "string", packageName)
                if (resId != 0) getString(resId) else ""
            } catch (_: Exception) {
                ""
            }
        }
        if (apiKey.isBlank()) {
            Log.e("TrailMate", "Mappls API key not configured.")
            return
        }
        try {
            Mappls.initialize(this, apiKey)
        } catch (t: Throwable) {
            Log.e("TrailMate", "Failed to initialize Mappls SDK", t)
        }
    }
}
