package com.rgcavalry.ticketscanner.server

import com.rgcavalry.ticketscanner.server.models.*
import com.rgcavalry.ticketscanner.server.models.responses.*
import retrofit2.Response
import retrofit2.http.*

interface ServerApi {
    @POST("sessions")
    suspend fun login(@Body body: UserLoginRequest): Response<Unit>

    @GET("private/whoami")
    suspend fun getCurrentUser(
        @Header("Cookie") cookie: String
    ): User

    @GET("cinemas")
    suspend fun getCinemasList(): CinemaListResponse

    @GET("cinemasession/{cinemaId}/{hallId}")
    suspend fun getSessionList(
        @Path("cinemaId") cinemaId: Int,
        @Path("hallId") hallId: Int
    ): SessionListResponse

    @GET("admin/cinemasession/{sessionId}/tickets_mobile")
    suspend fun getTicketList(
        @Header("Cookie") cookie: String,
        @Path("sessionId") sessionId: Int
    ): TicketListResponse

    @GET("films")
    suspend fun getFilms(): FilmListResponse
}