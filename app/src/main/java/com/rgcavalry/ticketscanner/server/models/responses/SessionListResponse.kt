package com.rgcavalry.ticketscanner.server.models.responses

import com.google.gson.annotations.SerializedName
import com.rgcavalry.ticketscanner.server.models.ServerSession

data class SessionListResponse(
    @SerializedName("Sessions") val sessions: List<ServerSession>?
)