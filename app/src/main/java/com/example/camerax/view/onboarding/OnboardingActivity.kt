package com.example.camerax.view.onboarding

import android.Manifest.permission.CAMERA
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.camerax.R
import com.example.camerax.databinding.ActivityOnboardingBinding
import com.example.camerax.mechanism.callShouldShowRequestPermissionRationale
import com.example.camerax.mechanism.openAppSystemSettings
import com.example.camerax.view.base.BaseActivity
import com.example.camerax.view.barcode.BarcodeReaderActivity
import com.example.camerax.view.camera.CameraActivity

class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {

    private var appPerms = arrayOf(CAMERA)
    private var checkIfIsBarcodeScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startCameraCheck()
        startBarcodeCheck()
    }

    private fun startCameraCheck() {
        binding.btStartCamera.setOnClickListener {
            checkIfIsBarcodeScreen = false
            activityResultLauncher.launch(appPerms)
        }
    }

    private fun startBarcodeCheck() {
        binding.btBarcodeReader.setOnClickListener {
            checkIfIsBarcodeScreen = true
            activityResultLauncher.launch(appPerms)
        }
    }

    private fun checkPermission(isBarcode: Boolean) {
        if (isBarcode) {
            startBarcode()
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        startActivity(CameraActivity.getStartIntent(this))
    }

    private fun startBarcode() {
        startActivity(BarcodeReaderActivity.getStartIntent(this))
    }

    private var activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            var allAreGranted = true
            for (permission in result.values) {
                allAreGranted = allAreGranted && permission
            }
            when {
                allAreGranted -> {
                    checkPermission(checkIfIsBarcodeScreen)
                }
                callShouldShowRequestPermissionRationale(this, CAMERA) -> {
                    this.openAppSystemSettings()
                }
                else -> {
                    this.openAppSystemSettings()
                }
            }
        }

}


