package com.rgcavalry.ticketscanner.server

import com.rgcavalry.ticketscanner.server.Status.*

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> success(data: T?) = Resource(SUCCESS, data, null)
        fun error(msg: String) = Resource(ERROR, null, msg)
        fun loading() = Resource(LOADING, null, null)
    }
}