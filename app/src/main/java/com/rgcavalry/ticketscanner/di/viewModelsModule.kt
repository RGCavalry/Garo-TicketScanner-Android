package com.rgcavalry.ticketscanner.di

import com.rgcavalry.ticketscanner.view_models.AuthViewModel
import com.rgcavalry.ticketscanner.view_models.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel {
        AuthViewModel(
            app = androidApplication(),
            serverRepo = get(),
        )
    }
    viewModel { MainViewModel() }
}