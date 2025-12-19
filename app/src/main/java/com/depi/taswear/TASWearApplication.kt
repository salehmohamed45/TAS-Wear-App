package com.depi.taswear

import android.app.Application
import com.depi.taswear.data.AppContainer

/**
 * Application class for initializing the AppContainer
 */
class TASWearApplication : Application() {
    
    lateinit var appContainer: AppContainer
        private set
    
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer()
    }
}
