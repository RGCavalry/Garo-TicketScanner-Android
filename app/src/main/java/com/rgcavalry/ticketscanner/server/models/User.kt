package com.rgcavalry.ticketscanner.server.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    @SerializedName("firstname") val firstName: String? = "Аноним",
    @SerializedName("lastname") val lastName: String? = "Анонимов",
    val email: String
)