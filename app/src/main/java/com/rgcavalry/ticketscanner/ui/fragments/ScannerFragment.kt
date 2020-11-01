package com.rgcavalry.ticketscanner.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.budiyev.android.codescanner.CodeScanner
import com.rgcavalry.ticketscanner.R
import com.rgcavalry.ticketscanner.utils.extensions.shortToast
import kotlinx.android.synthetic.main.fragment_scanner.*

class ScannerFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_scanner, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = initCodeScanner()

    private fun initCodeScanner() {
        codeScanner = CodeScanner(requireActivity(), scannerView).apply {
            setDecodeCallback {
                requireActivity().runOnUiThread {
                    requireContext().shortToast(it.text).show()
                }
            }
            setErrorCallback {
                requireActivity().runOnUiThread {
                    requireContext().shortToast("Error: ${it.message}").show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}
