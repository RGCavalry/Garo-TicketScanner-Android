package com.rgcavalry.ticketscanner.recyclers.sessions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rgcavalry.ticketscanner.R
import com.rgcavalry.ticketscanner.server.models.Session
import kotlinx.android.synthetic.main.view_session_card.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SessionsAdapter(
    private val context: Context
) : RecyclerView.Adapter<SessionsAdapter.SessionHolder>() {

    fun interface OnSessionClickListener {
        fun onSessionClicked(session: Session)
    }

    private var onSessionClickListener: OnSessionClickListener? = null

    fun setOnSessionClickListener(callback: OnSessionClickListener) {
        onSessionClickListener = callback
    }

    var sessionList = emptyList<Session>()
        set(value) {
            GlobalScope.launch(Dispatchers.Main) {
                val defectsDiffResult = withContext(Dispatchers.Default) {
                    DiffUtil.calculateDiff(SessionsDiffUtilCallback(sessionList, value))
                }
                field = value
                defectsDiffResult.dispatchUpdatesTo(this@SessionsAdapter)
            }
        }

    override fun getItemCount() = sessionList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SessionHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.view_session_card, parent, false)
    )

    override fun onBindViewHolder(holder: SessionHolder, position: Int) = holder.bind(position)

    inner class SessionHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        private val timeTV = view.startTime
        private val titleAndAgeRatingTV = view.titleAndAgeRating
        private val progressBar = view.progressBar
        private val scannedCounterTV = view.scannedCounter

        init {
            view.setOnClickListener{
                onSessionClickListener?.onSessionClicked(sessionList[adapterPosition])
            }
        }

        fun bind(position: Int) {
            val session = sessionList[position]
            val checkedTicketsNumber = session.tickets.count { it.checked }
            val ticketsNumber = session.tickets.size

            timeTV.text = session.startTime
            titleAndAgeRatingTV.text = context.getString(
                R.string.title_age_holder,
                session.film.name,
                session.film.ageRating
            )
            progressBar.apply {
                max = ticketsNumber
                progress = checkedTicketsNumber
            }
            scannedCounterTV.text = context.getString(
                R.string.scanner_holder,
                checkedTicketsNumber,
                ticketsNumber
            )
        }
    }
}