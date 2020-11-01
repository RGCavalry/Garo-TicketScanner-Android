package com.rgcavalry.ticketscanner.server.models

data class Session(
    val id: Int,
    val startTime: Long,
    val film: Film,
    val tickets: List<Ticket>
)