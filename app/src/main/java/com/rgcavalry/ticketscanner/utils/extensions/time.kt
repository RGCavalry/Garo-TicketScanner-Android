package com.rgcavalry.ticketscanner.utils.extensions

import java.lang.RuntimeException
import java.text.SimpleDateFormat

fun String.dateTimeToMillis(): Long {
    val pattern = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val date = pattern.parse(this)
    return date?.time ?: throw RuntimeException("Wrong time: $this")
}