package com.example.camerax.view.camera

import CameraManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import com.example.camerax.R
import com.example.camerax.databinding.ActivityCameraBinding
import com.example.camerax.mechanism.Constants.OCR.EXTRA_IMAGE_CAMERA
import com.example.camerax.view.base.BaseActivity
import com.example.camerax.view.photo.PhotoActivity

class CameraActivity : BaseActivity<ActivityCameraBinding>(R.layout.activity_camera) {

    companion object {
        fun getStartIntent(context: Context): Intent = Intent(context, CameraActivity::class.java)
    }

    private lateinit var cameraManager: CameraManager
    private var changeCamera = false
    private var flashMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startCamera()
        controlCameraFunctions()
        close()
    }

    private fun startCamera() {
        cameraManager = CameraManager(
            owner = this,
            context = applicationContext,
            viewPreview = binding.viewPreview,
            onResult = ::onResult
        )
        cameraManager.startCamera(frontCamera = false, flashMode = false)
    }

    private fun controlCameraFunctions() {
        binding.buttonsControls.cameraCapture.setOnClickListener { cameraManager.takePhoto() }
        binding.buttonsControls.changeCamera.setOnClickListener {
            cameraManager.startCamera(flashMode = flashMode(), frontCamera = changeCamera(true))
        }
        binding.buttonsControls.flashCamera.setOnClickListener {
            cameraManager.startCamera(flashMode = flashMode(), frontCamera = changeCamera(false))
        }
    }

    private fun changeCamera(isEnable: Boolean): Boolean {
        if (isEnable) changeCamera = !changeCamera
        return changeCamera
    }

    private fun flashMode(): Boolean {
        flashMode = !flashMode
        if (flashMode) binding.buttonsControls.flashCamera.setImageResource(R.drawable.ic_no_flash)
        else binding.buttonsControls.flashCamera.setImageResource(R.drawable.ic_flash)
        return flashMode
    }

    private fun onResult(result: String?) {
        if (!result.isNullOrEmpty()) startActivity(PhotoActivity.getStartIntent(this, result))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraManager.stop()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun close() {
        binding.close.setOnClickListener {
            finish()
        }
    }

}


