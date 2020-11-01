package com.rgcavalry.ticketscanner.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import com.rgcavalry.ticketscanner.R
import com.rgcavalry.ticketscanner.databinding.ActivityAuthBinding
import com.rgcavalry.ticketscanner.server.Status
import com.rgcavalry.ticketscanner.server.models.Cinema
import com.rgcavalry.ticketscanner.utils.extensions.*
import com.rgcavalry.ticketscanner.view_models.AuthViewModel
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : AppCompatActivity() {

    private val viewModel by viewModel<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityAuthBinding>(
            this,
            R.layout.activity_auth
        )
        binding.viewModel = viewModel
        setObservers()
        setHallListener()
    }

    private fun setObservers() {
        viewModel.cinemaListResource.observe(this) {
            when(it.status) {
                Status.SUCCESS -> fillCinemaList(it.data!!)
                Status.ERROR -> this.showShortToast(it.message!!.toString())
                Status.LOADING -> { }
            }
        }
        viewModel.loginResource.observe(this) {
            when(it.status) {
                Status.SUCCESS -> this.showLongToast(it.data.toString())
                Status.ERROR -> this.showShortToast(it.message.toString())
                Status.LOADING -> {}
            }
        }
    }

    private fun fillCinemaList(cinemaList: List<Cinema>) {
        (cinemaInput.editText as AutoCompleteTextView).apply {
            setText(cinemaList.firstOrNull()?.address)
            setAdapter(
                ArrayAdapter(
                    this@AuthActivity,
                    android.R.layout.simple_list_item_1,
                    cinemaList.map { it.address }
                )
            )
        }
    }

    private fun setHallListener() {
        hallGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when(checkedId) {
                    R.id.hall1Btn -> viewModel.selectedHallNumber = 1
                    R.id.hall2Btn -> viewModel.selectedHallNumber = 2
                    R.id.hall3Btn -> viewModel.selectedHallNumber = 3
                }
            }
        }
    }
}