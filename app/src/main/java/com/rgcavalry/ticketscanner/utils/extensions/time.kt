package com.rgcavalry.ticketscanner.utils.extensions

import java.text.SimpleDateFormat

fun String.toMillisTime() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(this)!!.time

fun Long.isMillisToday(): Boolean {
    val dateFormat = SimpleDateFormat("yyyyMMdd")
    return dateFormat.format(this) == dateFormat.format(System.currentTimeMillis())
}

fun Long.millisToTime(): String = SimpleDateFormat("HH:mm").format(this)