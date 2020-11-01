package com.rgcavalry.ticketscanner.utils.extensions

import androidx.fragment.app.Fragment
import com.rgcavalry.ticketscanner.ui.MainActivity

fun Fragment.mainActivity() = requireActivity() as MainActivity