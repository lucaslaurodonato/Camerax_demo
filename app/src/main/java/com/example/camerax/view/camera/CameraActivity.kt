package com.example.camerax.view.camera

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.example.camerax.R
import com.example.camerax.databinding.ActivityCameraBinding
import com.example.camerax.mechanism.toast
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
        initFunction()
    }

    private fun initFunction() {
        startCamera()
        controlCameraSelectCamera()
        controlFlashCamera()
        takePhoto()
        close()
    }

    private fun startCamera() {
        cameraManager = CameraManager(
            owner = this,
            context = applicationContext,
            viewPreview = binding.pvViewCamera,
            onSuccess = ::onSuccess,
            onError = ::onError
        )
        cameraManager.startCamera(onFrontCamera = false)
    }

    private fun takePhoto() {
        binding.incButtons.ivCameraCapture.setOnClickListener {
            cameraManager.takePhoto()
        }
    }

    private fun controlCameraSelectCamera() {
        binding.incButtons.ivChangeCamera.setOnClickListener {
            cameraManager.startCamera(onFrontCamera = changeCamera())
        }
    }

    private fun controlFlashCamera() {
        binding.incButtons.ivFlashCamera.setOnClickListener {
            cameraManager.enableFlash(flashMode())
        }
    }

    private fun changeCamera(): Boolean {
        changeCamera = !changeCamera
        if (changeCamera) binding.incButtons.ivFlashCamera.setImageResource(R.drawable.ic_no_flash)
        return changeCamera
    }

    private fun flashMode(): Boolean {
        flashMode = !flashMode
        if (flashMode) binding.incButtons.ivFlashCamera.setImageResource(R.drawable.ic_no_flash)
        else binding.incButtons.ivFlashCamera.setImageResource(R.drawable.ic_flash)
        return flashMode
    }

    private fun onSuccess(result: String?) {
        if (!result.isNullOrEmpty()) startActivity(PhotoActivity.getStartIntent(this, result))
        finish()
    }

    private fun onError(result: String?) {
        if (!result.isNullOrEmpty()) toast(result)
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
        binding.ivClose.setOnClickListener {
            finish()
        }
    }

}


