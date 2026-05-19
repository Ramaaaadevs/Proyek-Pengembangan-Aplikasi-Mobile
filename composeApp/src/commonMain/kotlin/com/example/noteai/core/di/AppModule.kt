package com.example.noteai.core.di

import com.example.noteai.data.local.NoteDatabase
import com.example.noteai.data.repository.TripRepositoryImpl
import com.example.noteai.domain.repository.TripRepository
import com.example.noteai.presentation.screens.home.TripViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val sharedModules = module {
    // Database
    single { NoteDatabase(get<com.example.noteai.core.util.DatabaseDriverFactory>().createDriver()) }

    // Repository
    single<TripRepository> { TripRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModelOf(::TripViewModel)
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
