package com.rgcavalry.ticketscanner.server.models

import com.google.gson.annotations.SerializedName

data class Film(
    val id: Int,
    val name: String,
    @SerializedName("age_rating") val ageRating: Int
)