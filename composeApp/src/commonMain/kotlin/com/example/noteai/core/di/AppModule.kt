package com.example.noteai.core.di

import com.example.noteai.core.network.HttpClientFactory
import com.example.noteai.data.local.NoteDatabase
import com.example.noteai.data.remote.api.GeminiService
import com.example.noteai.data.repository.AIRepositoryImpl
import com.example.noteai.data.repository.TripRepositoryImpl
import com.example.noteai.domain.repository.TripRepository
import com.example.noteai.presentation.screens.ai.AIViewModel
import com.example.noteai.presentation.screens.home.TripViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val sharedModules = module {
    single { NoteDatabase(get<com.example.noteai.core.util.DatabaseDriverFactory>().createDriver()) }
    single { HttpClientFactory.create() }
    single { GeminiService(get()) }
    single<TripRepository> { TripRepositoryImpl(get()) }
    single { AIRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModelOf(::TripViewModel)
    viewModelOf(::AIViewModel)
}

fun initKoin(
    platformModules: List<Module> = emptyList(),
    appDeclaration: KoinAppDeclaration = {}
) {
    startKoin {
        appDeclaration()
        modules(platformModules + sharedModules + viewModelModule)
    }
}
