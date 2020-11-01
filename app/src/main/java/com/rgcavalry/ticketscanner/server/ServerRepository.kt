package com.rgcavalry.ticketscanner.server

import com.rgcavalry.ticketscanner.persistence.DataStorage
import com.rgcavalry.ticketscanner.server.models.Cinema
import com.rgcavalry.ticketscanner.server.models.Session
import com.rgcavalry.ticketscanner.server.models.UserLoginRequest
import com.rgcavalry.ticketscanner.utils.extensions.toClientTime
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
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
        dataStorage.saveUser(user)
        dataStorage.saveSelectedCinema(selectedCinemaId)
        dataStorage.saveSelectedHall(selectedHallNumber)

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

    suspend fun getSessions() = try {
        val cookie = dataStorage.getCookie()
        val cinemaId = dataStorage.getSelectedCinema()
        val hallId = dataStorage.getSelectedHall()

        val sessionsFlow = flowOf(serverApi.getSessionList(cinemaId, hallId))
        val filmsFlow = flowOf(serverApi.getFilms())

        val result = sessionsFlow.zip(filmsFlow) { sessionsResponse, filmsResponse ->
            sessionsResponse.sessions.map { serverSession ->
                Session(
                    serverSession.id,
                    serverSession.startTime.toClientTime(),
                    filmsResponse.films.find { it.id == serverSession.movieId }!!,
                    serverApi.getTicketList(cookie, serverSession.id).tickets ?: emptyList()
                )
            }.filter { it.tickets.isNotEmpty() }
        }.first()
        responseHandler.handleSuccess(result)
    } catch (e: Exception) {
        responseHandler.handleException(e)
    }
}