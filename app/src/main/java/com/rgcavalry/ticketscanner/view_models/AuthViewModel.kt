package com.rgcavalry.ticketscanner.view_models

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rgcavalry.ticketscanner.R
import com.rgcavalry.ticketscanner.server.Resource
import com.rgcavalry.ticketscanner.server.ServerRepository
import com.rgcavalry.ticketscanner.server.models.Cinema
import com.rgcavalry.ticketscanner.server.models.User
import com.rgcavalry.ticketscanner.utils.SingleLiveEvent
import com.rgcavalry.ticketscanner.utils.extensions.getAppContext
import com.rgcavalry.ticketscanner.utils.extensions.isNotEmail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(
    app: Application,
    private val serverRepo: ServerRepository
) : AndroidViewModel(app) {

    var email = ""
    val emailError = ObservableField("")

    var password = ""
    val passwordError = ObservableField("")

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

    private val _loginResource = SingleLiveEvent<Resource<User>>()
    val loginResource: LiveData<Resource<User>> = _loginResource

    fun login() {
        if (isDataValid()) {
            viewModelScope.launch {
                _loginResource.value = Resource.loading()
                val response = withContext(Dispatchers.IO) {
                    serverRepo.login(email, password)
                }
                _loginResource.value = response
            }
        }
    }

    private fun isDataValid(): Boolean {
        var result = true
        if (email.isNotEmail()) {
            emailError.set(getAppContext().getString(R.string.wrong_email_error))
            result = false
        } else {
            emailError.set("")
        }
        return result
    }
}