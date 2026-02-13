package com.trailmate.app

import android.app.Application
import android.util.Log

class TrailMateApplication : Application() {

    companion object {
        private const val TAG = "TrailMateApplication"
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "🚀 TrailMateApplication.onCreate() called")
        Log.d(TAG, "📁 Mappls config loaded from assets/")
        Log.d(TAG, "✅ Ready for maps!")
    }
}