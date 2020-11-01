package com.rgcavalry.ticketscanner.di

import com.rgcavalry.ticketscanner.recyclers.sessions.SessionsAdapter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val recyclerAdaptersModule = module {
    single { SessionsAdapter(androidContext()) }
}