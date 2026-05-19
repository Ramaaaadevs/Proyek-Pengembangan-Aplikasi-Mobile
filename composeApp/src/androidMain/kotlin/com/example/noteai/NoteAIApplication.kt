package com.example.noteai

import android.app.Application
import com.example.noteai.core.di.androidModule
import com.example.noteai.core.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class NoteAIApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kembalikan suntikan androidModule ke dalam initKoin
        initKoin(
            platformModules = listOf(androidModule)
        ) {
            androidLogger()
            androidContext(this@NoteAIApplication)
        }
    }
}