package com.rgcavalry.ticketscanner.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.rgcavalry.ticketscanner.R
import com.rgcavalry.ticketscanner.persistence.DataStorage
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

	private val dataStorage by inject<DataStorage>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		checkLogin()
	}

	fun navigateButtonVisible(isVisible: Boolean) = supportActionBar?.run {
		setDisplayHomeAsUpEnabled(isVisible)
		setDisplayShowHomeEnabled(isVisible)
	}

	override fun onSupportNavigateUp() = run {
		onBackPressed()
		true
	}

	private fun checkLogin() {
		if (dataStorage.isLogged()) {
			setContentView(R.layout.activity_main)
		} else {
			startActivity(Intent(applicationContext, AuthActivity::class.java))
			finish()
		}
	}
}
