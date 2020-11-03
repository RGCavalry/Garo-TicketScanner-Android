package com.rgcavalry.ticketscanner.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.rgcavalry.ticketscanner.R
import com.rgcavalry.ticketscanner.persistence.DataStorage
import com.rgcavalry.ticketscanner.server.Resource
import com.rgcavalry.ticketscanner.server.Status
import com.rgcavalry.ticketscanner.server.models.Cinema
import com.rgcavalry.ticketscanner.utils.extensions.mainActivity
import com.rgcavalry.ticketscanner.utils.extensions.showShortToast
import com.rgcavalry.ticketscanner.view_models.MainViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.cinemaInput
import kotlinx.android.synthetic.main.fragment_profile.hallGroup
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfileFragment : Fragment() {

    private val viewModel by sharedViewModel<MainViewModel>()

    private val dataStorage by inject<DataStorage>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity().title = getString(R.string.profile)
        mainActivity().navigateButtonVisible(true)
        initCinemaList()
    }

    private fun initCinemaList() {
        viewModel.cinemaListResource.value?.let {
            updateCinemaList(it)
        } ?: setObservers()
    }

    private fun setObservers() {
        viewModel.cinemaListResource.observe(viewLifecycleOwner) { updateCinemaList(it) }
    }

    private fun updateCinemaList(cinemaList: Resource<List<Cinema>>) {
        when(cinemaList.status) {
            Status.SUCCESS -> fillData(cinemaList.data!!)
            Status.ERROR -> requireContext().showShortToast(cinemaList.message!!.toString())
            Status.LOADING -> {}
        }
    }

    private fun fillData(cinemaList: List<Cinema>) {
        val user = dataStorage.getUser()!!
        fullNameHolder.text = getString(
            R.string.full_name_holder,
            user.firstName ?: "Anonim",
            user.lastName ?: "Anonimov"
        )
        emailHolder.text = user.email

        val selectedCinemaId = dataStorage.getSelectedCinema()
        (cinemaInput.editText as AutoCompleteTextView).apply {
            setText(cinemaList.find { it.id == selectedCinemaId }?.address)
            setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    cinemaList.map { it.address }
                )
            )
        }


        val selectedHallNumber = dataStorage.getSelectedHall()
        hallGroup.check(when(selectedHallNumber) {
            3 -> R.id.hall3Btn
            2 -> R.id.hall2Btn
            else -> R.id.hall1Btn
        })

        setListeners()
    }

    private fun setListeners() {
        hallGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                dataStorage.saveSelectedHall(
                    when(checkedId) {
                        R.id.hall3Btn -> 3
                        R.id.hall2Btn -> 2
                        else -> 1
                    }
                )
                viewModel.loadSessions()
            }
        }
        (cinemaInput.editText as AutoCompleteTextView).setOnItemClickListener { _, _, index, _ ->
            dataStorage.saveSelectedCinema(dataStorage.cinemaList[index].id)
            viewModel.loadSessions()
        }
        logoutBtn.setOnClickListener {
            dataStorage.clearData()
            requireActivity().recreate()
        }
    }
}