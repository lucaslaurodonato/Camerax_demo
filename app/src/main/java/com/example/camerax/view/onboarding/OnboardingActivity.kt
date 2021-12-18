package com.example.camerax.view.onboarding

import android.content.Intent
import android.os.Bundle
import com.example.camerax.R
import com.example.camerax.databinding.ActivityOnboardingBinding
import com.example.camerax.mechanism.permission.AppPermissionUtils
import com.example.camerax.mechanism.permission.AppPermissionUtils.Companion.CAMERA_PERMISSION
import com.example.camerax.mechanism.permission.PermissionListener
import com.example.camerax.mechanism.permission.hasPermission
import com.example.camerax.mechanism.toast
import com.example.camerax.view.base.BaseActivity
import com.example.camerax.view.camera.CameraActivity
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding),
    PermissionListener {

    private val permissionUtils: AppPermissionUtils by inject { parametersOf(this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.startHomeButton.setOnClickListener {
            if (hasPermission(this@OnboardingActivity, CAMERA_PERMISSION)) {
                startCamera()
            } else {
                permissionUtils.requestPermissions(CAMERA_PERMISSION)
            }
        }
    }

    private fun startCamera() {
        startActivity(CameraActivity.getStartIntent(this))
        finish()
    }

    override fun onPermissionGranted(permissions: List<String>) {
        when (permissions) {
            CAMERA_PERMISSION -> {
                startCamera()
            }
        }
    }

    override fun onPermissionDenied(permissions: List<String>) {
        when (permissions) {
            CAMERA_PERMISSION -> {
                permissionUtils.openPermissions()
            }
        }
    }

    override fun onPermissionNeverAskAgain(permissions: List<String>) {
        when (permissions) {
            CAMERA_PERMISSION -> {
                permissionUtils.openPermissions()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AppPermissionUtils.PERMISSION_REQUEST_CODE -> {
                if (hasPermission(this, CAMERA_PERMISSION)) {
                    startCamera()
                }
            }
        }
    }

}


