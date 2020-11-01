package com.rgcavalry.ticketscanner.utils.extensions

import java.text.SimpleDateFormat

fun String.toClientTime(): String {
    val serverPattern = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val clientPattern = SimpleDateFormat.getDateInstance()
    return clientPattern.format(serverPattern.parse(this)!!)
}