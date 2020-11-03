package com.rgcavalry.ticketscanner.di

import com.rgcavalry.ticketscanner.recyclers.sessions.SessionsAdapter
import com.rgcavalry.ticketscanner.recyclers.tickets.TicketsAdapter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val recyclerAdaptersModule = module {
    single { SessionsAdapter(androidContext()) }
    factory { TicketsAdapter() }
}