package com.rgcavalry.ticketscanner.utils.extensions

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.rgcavalry.ticketscanner.App

fun AndroidViewModel.getAppContext(): Context = this.getApplication<App>().applicationContext