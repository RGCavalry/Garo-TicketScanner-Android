package com.rgcavalry.ticketscanner.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rgcavalry.ticketscanner.R
import com.rgcavalry.ticketscanner.recyclers.sessions.SessionsAdapter
import com.rgcavalry.ticketscanner.server.Resource
import com.rgcavalry.ticketscanner.server.Status
import com.rgcavalry.ticketscanner.server.models.Session
import com.rgcavalry.ticketscanner.utils.extensions.mainActivity
import com.rgcavalry.ticketscanner.utils.extensions.shortToast
import com.rgcavalry.ticketscanner.utils.extensions.showShortToast
import com.rgcavalry.ticketscanner.view_models.MainViewModel
import kotlinx.android.synthetic.main.fragment_session_list.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class SessionListFragment : Fragment() {

    private val viewModel by viewModel<MainViewModel>()

    private var recyclerAdapter: SessionsAdapter? = get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_session_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity().title = getString(R.string.sessions)
        initRecyclerView()
        setObservers()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerAdapter = null
    }

    private fun initRecyclerView() {
        recyclerView.adapter = recyclerAdapter
        viewModel.sessionsListResource.value?.let {
            updateSessions(it)
        } ?:run {
            viewModel.loadSessions()
        }
    }

    private fun setObservers() {
        viewModel.sessionsListResource.observe(viewLifecycleOwner) { updateSessions(it) }
    }

    private fun setListeners() {
        recyclerAdapter?.setOnSessionClickListener {
            navigateToSession(it)
        }
        swipeContainer.setOnRefreshListener {
            viewModel.loadSessions()
        }
    }

    private fun updateSessions(sessionsResource: Resource<List<Session>>) {
        when(sessionsResource.status) {
            Status.SUCCESS -> {
                recyclerAdapter?.sessionList = sessionsResource.data!!
                swipeContainer.isRefreshing = false
            }
            Status.ERROR -> {
                requireContext().showShortToast(sessionsResource.message)
                swipeContainer.isRefreshing = false
            }
            Status.LOADING -> swipeContainer.isRefreshing = true
        }
    }

    private fun navigateToSession(session: Session) {
        requireContext().showShortToast(session.film.name)
    }
}