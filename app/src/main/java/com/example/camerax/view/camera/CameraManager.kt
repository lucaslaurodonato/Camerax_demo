import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.camerax.mechanism.Constants.OCR.FILENAME_FORMAT
import com.example.camerax.mechanism.toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(
    private val owner: AppCompatActivity,
    private val context: Context,
    private val viewPreview: PreviewView,
    private val onResult: (result: String) -> Unit
) {

    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null

    fun stop() {
        cameraExecutor.shutdown()
    }

    fun startCamera(frontCamera: Boolean = false, flashMode: Boolean = false) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            lensFacing = when (frontCamera) {
                true -> CameraSelector.LENS_FACING_FRONT
                else -> CameraSelector.LENS_FACING_BACK
            }
            bindCameraUseCases(flashMode)
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindCameraUseCases(flashMode: Boolean) {
        val cameraProvider =
            cameraProvider ?: throw IllegalStateException("Camera initialization failed.")
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val previewView = getPreviewUseCase()
        imageCapture = ImageCapture.Builder().build()
        cameraProvider.unbindAll()
        try {
            camera =
                cameraProvider.bindToLifecycle(owner, cameraSelector, previewView, imageCapture)
            camera?.cameraControl?.enableTorch(flashMode)
            previewView.setSurfaceProvider(viewPreview.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed $exc")
        }
    }

    private fun getPreviewUseCase(): Preview {
        return Preview.Builder()
            .setTargetRotation(viewPreview.display.rotation)
            .build()
    }

    fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            owner.cacheDir,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(owner), object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    onResult(Uri.fromFile(photoFile).toString())
                }

                override fun onError(exception: ImageCaptureException) {
                    onResult(exception.toString())
                }
            }
        )
    }

}
