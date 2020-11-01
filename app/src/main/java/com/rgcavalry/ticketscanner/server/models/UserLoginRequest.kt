package com.rgcavalry.ticketscanner.server.models

data class UserLoginRequest(
    val email: String,
    val password: String
)