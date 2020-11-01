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

    @GET("cinemasession/{cinemaId}")
    suspend fun getSessionList(
        @Path("cinemaId") cinemaId: Int
    ): SessionListResponse

    @GET("admin/cinemasession/{cinemaId}/{hallId}/tickets")
    suspend fun getTicketList(
        @Header("Cookie") cookie: String,
        @Path("cinemaId") cinemaId: Int,
        @Path("hallId") hallId: Int
    ): TicketListResponse

    @GET("films")
    suspend fun getFilms(): FilmListResponse

    @GET("cinemasession/{cinemaID}/{hallID}/places")
    suspend fun getPlaces(
        @Path("cinemaId") cinemaId: Int,
        @Path("hallId") hallId: Int
    ): PlaceListResponse

    @GET("admin/user/{userId}")
    suspend fun getUser(
        @Header("Cookie") cookie: String,
        @Path("userId") userId: Int
    ): User
}