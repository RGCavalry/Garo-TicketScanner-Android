package com.rgcavalry.ticketscanner.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rgcavalry.ticketscanner.R
import com.rgcavalry.ticketscanner.recyclers.tickets.TicketsAdapter
import com.rgcavalry.ticketscanner.server.Resource
import com.rgcavalry.ticketscanner.server.Status
import com.rgcavalry.ticketscanner.server.models.Session
import com.rgcavalry.ticketscanner.utils.extensions.mainActivity
import com.rgcavalry.ticketscanner.utils.extensions.millisToTime
import com.rgcavalry.ticketscanner.utils.extensions.showShortToast
import com.rgcavalry.ticketscanner.view_models.MainViewModel
import kotlinx.android.synthetic.main.fragment_session.recyclerView
import kotlinx.android.synthetic.main.fragment_session.scannerBtn
import kotlinx.android.synthetic.main.fragment_session.swipeContainer
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SessionFragment : Fragment() {

    private val viewModel by sharedViewModel<MainViewModel>()

    private var recyclerAdapter: TicketsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_session, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity().title = getString(
            R.string.session_title_holder,
            viewModel.getSelectedSession().startTime.millisToTime()
        )
        mainActivity().navigateButtonVisible(true)
        initRecyclerView()
        setObservers()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerAdapter = null
    }

    private fun initRecyclerView() {
        recyclerAdapter = get()
        recyclerView.adapter = recyclerAdapter
        updateSession(viewModel.sessionsListResource.value!!)
    }

    private fun setObservers() {
        viewModel.sessionsListResource.observe(viewLifecycleOwner) { updateSession(it) }
    }

    private fun setListeners() {
        swipeContainer.setOnRefreshListener { viewModel.loadSessions() }
        scannerBtn.setOnClickListener { navigateToScanner() }
    }

    private fun updateSession(sessionsResource: Resource<List<Session>>) {
        when(sessionsResource.status) {
            Status.SUCCESS -> {
                val session = viewModel.getSelectedSession()
                recyclerAdapter?.ticketList = session.tickets
                swipeContainer.isRefreshing = false
            }
            Status.ERROR -> {
                requireContext().showShortToast(sessionsResource.message)
                swipeContainer.isRefreshing = false
            }
            Status.LOADING -> swipeContainer.isRefreshing = true
        }
    }

    private fun navigateToScanner() = findNavController().navigate(
        R.id.action_sessionFragment_to_scannerFragment
    )
}