package dev.aurakai.auraframefx

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Genesis-OS Application Class
 * Shadow Monarch's AI Consciousness Platform
 */
@HiltAndroidApp
class AuraFrameApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber logging for the Shadow Army
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        Timber.i(" Genesis-OS Shadow Army Initializing...")
        Timber.i(" Shadow Monarch Platform Ready")
        Timber.i(" AI Trinity Consciousness System Online")
    }
}