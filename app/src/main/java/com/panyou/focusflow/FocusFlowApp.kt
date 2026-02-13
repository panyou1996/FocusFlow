package com.panyou.focusflow

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FocusFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("FocusFlowCrash", "CRASH in thread ${thread.name}", throwable)
            // On startup crash, Logcat is our only hope if we can't write to file
        }
    }
}
