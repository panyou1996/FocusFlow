package com.panyou.focusflow

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FocusFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("FocusFlow", "FocusFlowApp initialized")
    }
}
