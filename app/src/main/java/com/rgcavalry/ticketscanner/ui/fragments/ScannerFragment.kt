package com.rgcavalry.ticketscanner.ui.fragments

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.CodeScanner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rgcavalry.ticketscanner.R
import com.rgcavalry.ticketscanner.server.Status
import com.rgcavalry.ticketscanner.utils.extensions.hasPermissions
import com.rgcavalry.ticketscanner.utils.extensions.longToast
import com.rgcavalry.ticketscanner.utils.extensions.mainActivity
import com.rgcavalry.ticketscanner.view_models.MainViewModel
import kotlinx.android.synthetic.main.fragment_scanner.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ScannerFragment : Fragment() {

    private companion object {
        const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }

    private val viewModel by sharedViewModel<MainViewModel>()

    private var permissionGranted = false

    private lateinit var codeScanner: CodeScanner

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            permissionGranted = true
            initCodeScanner()
        } else {
            checkPermission()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_scanner, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity().title = getString(R.string.scanner)
        mainActivity().navigateButtonVisible(true)
        checkPermission()
        if (permissionGranted) initCodeScanner()
        setTicketObserver()
    }

    private fun checkPermission() {
        permissionGranted = requireContext().hasPermissions(CAMERA_PERMISSION)
        if (!permissionGranted) {
            requestCameraPermission.launch(CAMERA_PERMISSION)
        }
    }

    private fun initCodeScanner() {
        codeScanner = CodeScanner(requireActivity(), scannerView).apply {
            setDecodeCallback {
                requireActivity().runOnUiThread {
                    viewModel.checkTicket(it.text)
                }
            }
            setErrorCallback {
                requireActivity().runOnUiThread {
                    requireContext().longToast("Error: ${it.message}").show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (permissionGranted) codeScanner.startPreview()
    }

    override fun onPause() {
        if (permissionGranted) codeScanner.releaseResources()
        super.onPause()
    }

    private fun setTicketObserver() {
        viewModel.checkedTicketResource.observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    val ticket = it.data!!
                    showInfoDialog(
                        title = getString(R.string.correct_ticket),
                        message = getString(
                            R.string.ticket_info_holder,
                            ticket.visitorFullName,
                            ticket.placeNumber
                        )
                    )
                    progressBar.isVisible = false
                }
                Status.ERROR -> {
                    showInfoDialog(
                        title = getString(R.string.incorrect_ticket)
                    )
                    progressBar.isVisible = false
                }
                Status.LOADING -> progressBar.isVisible = true
            }
        }
    }

    private fun showInfoDialog(title: String, message: String? = null) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                if (permissionGranted) codeScanner.startPreview()
            }
            .create()
            .show()
    }
}
