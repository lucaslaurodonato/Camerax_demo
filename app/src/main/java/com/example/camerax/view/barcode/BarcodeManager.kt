package com.example.camerax.view.barcode

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BarcodeManager(
    private val owner: AppCompatActivity,
    private val context: Context,
    private val viewPreview: PreviewView,
    private val onSearchBarCode: (result: String) -> Unit
) {

    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null

    fun stop() {
        cameraExecutor.shutdown()
    }

    fun enableFlash(onFlashMode: Boolean) {
        camera?.cameraControl?.enableTorch(onFlashMode)
    }

    fun startCameraToBarcode() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(context))
    }

    private fun getImageAnalysis(): ImageAnalysis {
        return ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { barcode ->
                    onSearchBarCode(barcode)
                })
            }
    }

    private fun bindCameraUseCases() {
        cameraProvider?.let { cameraProvider ->
            val cameraSelector = getCameraSelector()
            val previewView = getPreviewUseCase()
            imageCapture = getImageCapture()
            cameraProvider.unbindAll()
            try {

                camera = cameraProvider.bindToLifecycle(
                    owner,
                    cameraSelector,
                    previewView,
                    getImageAnalysis(),
                    imageCapture
                )

                previewView.setSurfaceProvider(viewPreview.surfaceProvider)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed $exc")
            }
        } ?: throw IllegalStateException("Camera to barcode initialization failed.")
    }

    private fun getCameraSelector(): CameraSelector {
        return CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
    }

    private fun getImageCapture(): ImageCapture {
        return ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    private fun getPreviewUseCase(): Preview {
        return Preview.Builder()
            .setTargetRotation(viewPreview.display.rotation)
            .build()
    }

}
