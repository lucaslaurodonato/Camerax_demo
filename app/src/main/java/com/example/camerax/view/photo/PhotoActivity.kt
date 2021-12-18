package com.example.camerax.view.photo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.example.camerax.R
import com.example.camerax.databinding.ActivityPhotoBinding
import com.example.camerax.mechanism.Constants.OCR.EXTRA_IMAGE_CAMERA
import com.example.camerax.view.base.BaseActivity
import com.example.camerax.view.camera.CameraActivity

class PhotoActivity : BaseActivity<ActivityPhotoBinding>(R.layout.activity_photo) {

    companion object {
        fun getStartIntent(context: Context, photoUri: String): Intent =
            Intent(context, PhotoActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_CAMERA, photoUri)
            }
    }

    private var picture: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiveIntent()
        takeAnotherPhoto()
    }

    private fun receiveIntent() {
        picture = intent?.getStringExtra(EXTRA_IMAGE_CAMERA) as String
        setupView()
    }

    private fun setupView() {
        val pictureUri: Uri? = Uri.parse(picture)
        binding.includePicture.picture.setImageURI(pictureUri)
    }

    private fun takeAnotherPhoto() {
        binding.takeAnotherPicture.setOnClickListener {
            startActivity(CameraActivity.getStartIntent(this))
            finish()
        }
        binding.close.setOnClickListener {
            finish()
        }
    }


}


