package com.rgcavalry.ticketscanner.recyclers.tickets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rgcavalry.ticketscanner.R
import com.rgcavalry.ticketscanner.server.models.Ticket
import kotlinx.android.synthetic.main.view_ticket.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TicketsAdapter : RecyclerView.Adapter<TicketsAdapter.TicketHolder>() {

    var ticketList = emptyList<Ticket>()
        set(value) {
            GlobalScope.launch(Dispatchers.Main) {
                val defectsDiffResult = withContext(Dispatchers.Default) {
                    DiffUtil.calculateDiff(TicketsDiffUtilCallback(ticketList, value))
                }
                field = value
                defectsDiffResult.dispatchUpdatesTo(this@TicketsAdapter)
            }
        }

    override fun getItemCount() = ticketList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TicketHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_ticket, parent, false)
    )

    override fun onBindViewHolder(holder: TicketHolder, position: Int) = holder.bind(position)

    inner class TicketHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        private val fullNameTV = view.fullName
        private val placeTV = view.place
        private val indicatorIV = view.checkedIndicator

        fun bind(position: Int) {
            val ticket = ticketList[position]
            fullNameTV.text = ticket.visitorFullName ?: "Аноним"
            placeTV.text = ticket.placeNumber
            indicatorIV.isVisible = ticket.checked
        }
    }
}