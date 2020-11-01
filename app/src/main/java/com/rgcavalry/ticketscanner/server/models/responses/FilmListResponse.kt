package com.rgcavalry.ticketscanner.server.models.responses

import com.google.gson.annotations.SerializedName
import com.rgcavalry.ticketscanner.server.models.Film

data class FilmListResponse(
    @SerializedName("Movies") val films: List<Film>
)