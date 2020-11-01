package com.rgcavalry.ticketscanner.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgcavalry.ticketscanner.server.Resource
import com.rgcavalry.ticketscanner.server.ServerRepository
import com.rgcavalry.ticketscanner.server.models.Session
import com.rgcavalry.ticketscanner.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class MainViewModel(
    private val serverRepo: ServerRepository
): ViewModel() {

    private val _sessionsListResource = SingleLiveEvent<Resource<List<Session>>>()
    val sessionsListResource: LiveData<Resource<List<Session>>> = _sessionsListResource

    fun loadSessions() {
        viewModelScope.launch {
            val time = measureTimeMillis {
                _sessionsListResource.value = Resource.loading()
                val response = withContext(Dispatchers.IO) {
                    serverRepo.getSessions()
                }
                _sessionsListResource.value = response
            }
            Log.wtf("time", time.toString())
        }
    }
}