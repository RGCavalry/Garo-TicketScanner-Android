package com.rgcavalry.ticketscanner.server.models

import com.google.gson.annotations.SerializedName

data class ServerSession (
    @SerializedName("id") val id : Int,
    @SerializedName("hall_id") val hallId : Int,
    @SerializedName("movie_id") val movieId : Int,
    @SerializedName("price") val price : String,
    @SerializedName("sessiontype_id") val sessionTypeId : Int,
    @SerializedName("start_time") val startTime : String
)