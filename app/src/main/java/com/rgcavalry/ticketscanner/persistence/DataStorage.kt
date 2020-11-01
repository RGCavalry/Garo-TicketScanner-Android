package com.rgcavalry.ticketscanner.persistence

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.rgcavalry.ticketscanner.server.models.Cinema
import com.rgcavalry.ticketscanner.server.models.User

class DataStorage(
    context: Context
) {
    private companion object {
        const val PREFERENCES_KEY = "preferencesKey"
        const val COOKIE_KEY ="cookieKey"
        const val USER_KEY = "userKey"
        const val CINEMA_KEY = "cinemaKey"
        const val HALL_KEY = "hallKey"
        const val CHECKED_TICKET_LIST_KEY = "checkedListKey"
    }

    private val gson = Gson()

    var cinemaList = emptyList<Cinema>()

    private val prefs = context.getSharedPreferences(
        PREFERENCES_KEY,
        Context.MODE_PRIVATE
    )

    fun saveCookie(cookie: String) = prefs.edit {
        putString(COOKIE_KEY, cookie)
    }

    fun getCookie() = prefs.getString(COOKIE_KEY, null) ?: ""

    fun saveUser(user: User) = prefs.edit {
        putString(USER_KEY, gson.toJson(user))
    }

    fun getUser() = prefs.getString(USER_KEY, null)?.let {
        gson.fromJson(it, User::class.java)
    }

    fun isLogged() = getUser() != null

    fun saveSelectedCinema(cinemaId: Int) = prefs.edit {
        putInt(CINEMA_KEY, cinemaId)
    }

    fun getSelectedCinema() = prefs.getInt(CINEMA_KEY, -1)

    fun saveSelectedHall(hallId: Int) = prefs.edit {
        putInt(HALL_KEY, hallId)
    }

    fun getSelectedHall() = prefs.getInt(HALL_KEY, -1)

    fun saveCheckedTicketList(ticketIdList: List<Int>) = prefs.edit {
        putStringSet(CHECKED_TICKET_LIST_KEY, ticketIdList.map { it.toString() }.toSet())
    }

    fun getCheckedTicketList() = prefs.getStringSet(CHECKED_TICKET_LIST_KEY, emptySet())

    fun clearData() {
        val checkedTickets = getCheckedTicketList()
        prefs.edit {
            clear()
            putStringSet(CHECKED_TICKET_LIST_KEY, checkedTickets)
        }
    }
}