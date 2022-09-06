package com.voterswik

import android.app.Application

import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class VotersWikUserApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}