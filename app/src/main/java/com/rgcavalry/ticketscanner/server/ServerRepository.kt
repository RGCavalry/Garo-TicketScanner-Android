package com.rgcavalry.ticketscanner.server

import com.rgcavalry.ticketscanner.persistence.DataStorage
import com.rgcavalry.ticketscanner.server.models.Cinema
import com.rgcavalry.ticketscanner.server.models.Session
import com.rgcavalry.ticketscanner.server.models.UserLoginRequest
import java.lang.Exception

class ServerRepository(
    private val serverApi: ServerApi,
    private val responseHandler: ResponseHandler,
    private val dataStorage: DataStorage
) {
    private companion object {
        const val COOKIE_HEADER_NAME = "Set-Cookie"
    }

    suspend fun login(email: String, password: String) = try {
        val loginResponse = serverApi.login(UserLoginRequest(email, password))
        val cookie = loginResponse.headers().get(COOKIE_HEADER_NAME)!!
        dataStorage.saveCookie(cookie)

        val user = serverApi.getCurrentUser(cookie)
        dataStorage.saveUser(user)

        responseHandler.handleSuccess(user)
    } catch (e: Exception) {
        responseHandler.handleException(e)
    }

    suspend fun getCinemaList(): Resource<List<Cinema>> {
        if (dataStorage.cinemaList.isEmpty()) {
            try {
                dataStorage.cinemaList = serverApi.getCinemasList().cinemas
            } catch (e: Exception) {
                return responseHandler.handleException(e)
            }
        }
        return responseHandler.handleSuccess(dataStorage.cinemaList)
    }
}