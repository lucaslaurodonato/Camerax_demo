package com.example.camerax.view.barcode

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(private val barcodeListener: BarcodeListener) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                .let { image ->
                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                barcodeListener(barcode.rawValue ?: "")
                            }
                        }
                        .addOnFailureListener {
                            Log.e(ContentValues.TAG, "Use case binding failed $it")
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                }
        } else {
            Log.e(ContentValues.TAG, "Image is empty")
        }
    }
}