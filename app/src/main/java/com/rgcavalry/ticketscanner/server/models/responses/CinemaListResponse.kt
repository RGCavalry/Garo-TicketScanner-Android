package com.rgcavalry.ticketscanner.server.models.responses

import com.google.gson.annotations.SerializedName
import com.rgcavalry.ticketscanner.server.models.Cinema

data class CinemaListResponse(
    @SerializedName("Cinemas") val cinemas: List<Cinema>
)