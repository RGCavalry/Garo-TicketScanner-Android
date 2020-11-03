package com.rgcavalry.ticketscanner.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rgcavalry.ticketscanner.server.Resource
import com.rgcavalry.ticketscanner.server.ServerRepository
import com.rgcavalry.ticketscanner.server.models.Cinema
import com.rgcavalry.ticketscanner.server.models.Session
import com.rgcavalry.ticketscanner.utils.extensions.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val serverRepo: ServerRepository
): ViewModel() {

    private val _cinemaListResource = SingleLiveEvent<Resource<List<Cinema>>>()
    val cinemaListResource: LiveData<Resource<List<Cinema>>> = _cinemaListResource

    init {
        viewModelScope.launch {
            _cinemaListResource.value = Resource.loading()
            val response = withContext(Dispatchers.IO) {
                serverRepo.getCinemaList()
            }
            _cinemaListResource.value = response
        }
    }

    private val _sessionsListResource = SingleLiveEvent<Resource<List<Session>>>()
    val sessionsListResource: LiveData<Resource<List<Session>>> = _sessionsListResource

    fun loadSessions() {
        viewModelScope.launch {
            _sessionsListResource.value = Resource.loading()
            val response = withContext(Dispatchers.IO) {
                serverRepo.getSessions()
            }
            _sessionsListResource.value = response
        }
    }

    var selectedSessionId = -1

    fun getSelectedSession() = sessionsListResource.value!!.data!!.find {
        it.id == selectedSessionId
    }!!
}