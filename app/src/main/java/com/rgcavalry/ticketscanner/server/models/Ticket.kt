package com.rgcavalry.ticketscanner.server.models

import com.google.gson.annotations.SerializedName

data class Ticket(
    val id: Int,
    @SerializedName("visitor_full_name") val visitorFullName: String = "Ivan Ivanov",
    @SerializedName("place_number") val placeNumber: String,
    var checked: Boolean = false
)