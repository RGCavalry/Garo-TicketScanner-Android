package com.rgcavalry.ticketscanner.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rgcavalry.ticketscanner.R
import com.rgcavalry.ticketscanner.server.Status
import com.rgcavalry.ticketscanner.utils.extensions.showShortToast
import com.rgcavalry.ticketscanner.view_models.MainViewModel
import kotlinx.android.synthetic.main.fragment_session_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SessionListFragment : Fragment() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_session_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadSessions()
        setObservers()
    }

    private fun setObservers() {
        viewModel.sessionsListResource.observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> textHolder.text = it.data!!.joinToString()
                Status.ERROR -> requireContext().showShortToast(it.message)
                Status.LOADING -> {}
            }
        }
    }
}