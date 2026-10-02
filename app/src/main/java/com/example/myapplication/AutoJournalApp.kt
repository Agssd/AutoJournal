package com.example.myapplication

import android.app.Application
import com.example.myapplication.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AutoJournalApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AutoJournalApp)
            modules(appModule)
        }
    }
}