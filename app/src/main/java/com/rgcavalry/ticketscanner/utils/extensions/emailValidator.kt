package com.rgcavalry.ticketscanner.utils.extensions

import android.util.Patterns

fun String.isEmail() = isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
fun String.isNotEmail() = !isEmail()