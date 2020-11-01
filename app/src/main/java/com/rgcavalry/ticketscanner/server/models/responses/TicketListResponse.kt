package com.rgcavalry.ticketscanner.server.models.responses

import com.google.gson.annotations.SerializedName
import com.rgcavalry.ticketscanner.server.models.Ticket

data class TicketListResponse(
    @SerializedName("TicketsMobile") val tickets: List<Ticket>?
)