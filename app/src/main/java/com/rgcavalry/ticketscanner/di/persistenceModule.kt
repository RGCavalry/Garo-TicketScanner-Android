package com.rgcavalry.ticketscanner.di

import com.rgcavalry.ticketscanner.persistence.DataStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val persistenceModule = module {
    single {
        DataStorage(
            context = androidContext()
        )
    }
}