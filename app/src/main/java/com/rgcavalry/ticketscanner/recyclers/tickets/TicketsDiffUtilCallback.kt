package com.rgcavalry.ticketscanner.recyclers.tickets

import androidx.recyclerview.widget.DiffUtil
import com.rgcavalry.ticketscanner.server.models.Ticket

class TicketsDiffUtilCallback (
    private val oldList: List<Ticket>,
    private val newList: List<Ticket>
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
        return old.placeNumber == new.placeNumber &&
                old.visitorFullName == new.visitorFullName &&
                old.checked == new.checked
    }
}