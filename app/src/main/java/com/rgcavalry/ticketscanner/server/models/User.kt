package com.rgcavalry.ticketscanner.server.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    @SerializedName("firstname") val firstName: String = "Ivan",
    @SerializedName("lastname") val lastName: String = "Ivanov",
    val email: String,
    val phone: String = "none",
    @SerializedName("accessrule_id") val accessRuleId: Int
)