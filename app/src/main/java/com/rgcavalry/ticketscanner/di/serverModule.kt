package com.rgcavalry.ticketscanner.di

import com.rgcavalry.ticketscanner.BuildConfig
import com.rgcavalry.ticketscanner.server.ResponseHandler
import com.rgcavalry.ticketscanner.server.ServerApi
import com.rgcavalry.ticketscanner.server.ServerRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val serverModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<ServerApi> { get<Retrofit>().create() }
    single { ResponseHandler() }
    single {
        ServerRepository(
            serverApi = get(),
            responseHandler = get(),
            dataStorage = get()
        )
    }
}