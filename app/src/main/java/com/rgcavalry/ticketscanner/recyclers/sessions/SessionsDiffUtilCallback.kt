package com.rgcavalry.ticketscanner.recyclers.sessions

import androidx.recyclerview.widget.DiffUtil
import com.rgcavalry.ticketscanner.server.models.Session

class SessionsDiffUtilCallback (
    private val oldList: List<Session>,
    private val newList: List<Session>
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old.id == new.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old.film == new.film &&
                old.startTime == new.startTime &&
                old.tickets == new.tickets
    }
}