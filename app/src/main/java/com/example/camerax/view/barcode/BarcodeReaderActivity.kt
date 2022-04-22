package com.example.camerax.view.barcode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.camerax.R
import com.example.camerax.databinding.ActivityBarcodeReaderBinding
import com.example.camerax.mechanism.toast
import com.example.camerax.view.base.BaseActivity

class BarcodeReaderActivity :
    BaseActivity<ActivityBarcodeReaderBinding>(R.layout.activity_barcode_reader) {

    companion object {
        fun getStartIntent(context: Context): Intent =
            Intent(context, BarcodeReaderActivity::class.java)
    }

    private lateinit var barcodeManager: BarcodeManager
    private var flashMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFunctions()
    }

    private fun initFunctions() {
        startBarcode()
        close()
        controlFlashBarcode()
        startAnimation()
    }

    private fun flashMode(): Boolean {
        flashMode = !flashMode
        if (flashMode) binding.ivFlash.setImageResource(R.drawable.ic_no_flash_white)
        else binding.ivFlash.setImageResource(R.drawable.ic_flash_white)
        return flashMode
    }

    private fun controlFlashBarcode() {
        binding.ivFlash.setOnClickListener {
            barcodeManager.enableFlash(flashMode())
        }
    }

    private fun startAnimation() {
        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.barcode_animator)
        binding.llAnimation.startAnimation(animation)
    }

    private fun startBarcode() {
        barcodeManager = BarcodeManager(
            owner = this,
            context = applicationContext,
            viewPreview = binding.pvViewCamera,
            onSearchBarCode = ::onSearchBarCode
        )
        barcodeManager.startCameraToBarcode()
    }

    private fun onSearchBarCode(result: String?) {
        result?.let {
            binding.tvBarcodeReader.text = result
        } ?: toast(getString(R.string.bar_code_error_to_read))
    }

    override fun onDestroy() {
        super.onDestroy()
        barcodeManager.stop()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun close() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

}