package com.rgcavalry.ticketscanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

private const val CAMERA_PERMISSION_REQUEST_CODE = 1

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setFullscreen()
		checkCameraPermission()
	}

	private fun setFullscreen() {
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
	}

	private fun checkCameraPermission() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(
					this,
					arrayOf(Manifest.permission.CAMERA),
					CAMERA_PERMISSION_REQUEST_CODE
			)
		} else {
			openFragmentWithoutBackStack(ScannerFragment())
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				openFragmentWithoutBackStack(ScannerFragment())
			} else {
				Toast.makeText(this, getString(R.string.permission_denied_camera), Toast.LENGTH_SHORT).show()
				checkCameraPermission()
			}
		}
	}

	fun openFragment(fragment: Fragment) {
		supportFragmentManager.beginTransaction()
				.replace(R.id.fragmentContainer, fragment)
				.addToBackStack(null)
				.commit()
	}

	private fun openFragmentWithoutBackStack(fragment: Fragment) {
		supportFragmentManager.beginTransaction()
				.replace(R.id.fragmentContainer, fragment)
				.commitNow()
	}
}
