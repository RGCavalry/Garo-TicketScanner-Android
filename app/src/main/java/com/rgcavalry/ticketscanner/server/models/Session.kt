package com.rgcavalry.ticketscanner.server.models

data class Session(
    val id: Int,
    val startTime: String,
    val film: Film,
    val tickets: List<Ticket>
)