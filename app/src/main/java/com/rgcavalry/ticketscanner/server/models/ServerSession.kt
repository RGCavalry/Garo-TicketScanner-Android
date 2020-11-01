package com.rgcavalry.ticketscanner.server.models

import com.google.gson.annotations.SerializedName

data class ServerSession (
    @SerializedName("id") val id : Int,
    @SerializedName("movie_id") val movieId : Int,
    @SerializedName("start_time") val startTime : String
)