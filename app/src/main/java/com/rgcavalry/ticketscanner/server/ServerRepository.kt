package com.rgcavalry.ticketscanner.server

import android.util.Log
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

    suspend fun login(
        email: String,
        password: String,
        selectedCinemaId: Int,
        selectedHallNumber: Int
    ) = try {
        val loginResponse = serverApi.login(UserLoginRequest(email, password))
        val cookie = loginResponse.headers().get(COOKIE_HEADER_NAME)!!

        val user = serverApi.getCurrentUser(cookie)
        dataStorage.saveCookie(cookie)
//        dataStorage.saveUser(user)
//        dataStorage.saveSelectedCinema(selectedCinemaId)
//        dataStorage.saveSelectedHall(selectedHallId)
        Log.wtf("hey", "Cinema: $selectedCinemaId | Hall: $selectedHallNumber")

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

//    suspend fun getSessions(): Resource<List<Session>> {
//        val cookie = dataStorage.getCookie()
//        val cinemaId = dataStorage.getSelectedCinema()
//        val hallId = dataStorage.getSelectedHall()
//
//        val sessionsInCinema = serverApi.getSessionList(cinemaId)
//        val ticketsInCinemaInHall = serverApi.getTicketList(cookie, cinemaId, hallId)
//        val allFilms = serverApi.getFilms()
//        val placesInCinemaInHall = serverApi.getPlaces(cinemaId, hallId)
////        val userById = serverApi.getUser()
//    }
}