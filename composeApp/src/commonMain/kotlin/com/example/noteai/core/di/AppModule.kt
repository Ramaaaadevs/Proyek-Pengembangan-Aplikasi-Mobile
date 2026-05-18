package com.example.noteai.core.di

import com.example.noteai.data.repository.TripRepositoryImpl
import com.example.noteai.domain.repository.TripRepository
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val sharedModules = module {
    single<TripRepository> { TripRepositoryImpl(get()) }
}

val viewModelModule = module {
}

// Kembalikan parameter platformModules seperti bawaan template asli
fun initKoin(
    platformModules: List<Module> = emptyList(),
    appDeclaration: KoinAppDeclaration = {}
) {
    startKoin {
        appDeclaration()
        modules(platformModules + sharedModules + viewModelModule)
    }
}