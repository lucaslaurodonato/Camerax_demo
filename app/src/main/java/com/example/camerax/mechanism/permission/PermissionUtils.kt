package com.example.camerax.mechanism.permission

import android.Manifest.permission.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.random.Random

interface PermissionListener {
    fun onPermissionGranted(permissions: List<String>)
    fun onPermissionDenied(permissions: List<String>)
    fun onPermissionNeverAskAgain(permissions: List<String>)
}

class AppPermissionUtils(private val activity: AppCompatActivity,
                         private val listener: PermissionListener
) {

    companion object {
        //PERMISSIONS
        val CAMERA_PERMISSION = listOf(CAMERA)
        val APP_PERMISSIONS = listOf(CAMERA, READ_PHONE_STATE)
        const val PERMISSION_REQUEST_CODE = 735
    }


    //Contains a map with permission code and permissions needed
    private val requests: MutableList<Pair<List<String>, Int>> = mutableListOf()

    fun requestPermissions(permissions: List<String>) {
        if (hasPermission(activity, permissions)) {
            listener.onPermissionGranted(permissions)
        } else {
            //Generate a random permission code, for the array of permissions given
            requests.find {
                it.first == permissions
            }?.let {
                ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), it.second)
            } ?: run {
                Random.nextInt(100, 2000).let { permissionCode ->
                    requests.add(Pair(permissions, permissionCode))
                    ActivityCompat.requestPermissions(activity, permissions.toTypedArray(),
                        permissionCode)
                }
            }
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                   grantResults: IntArray) {
        requests.find {
            it.second == requestCode
        }?.let { request ->
            permissions.filterIndexed { index, _ ->
                grantResults[index] != PackageManager.PERMISSION_GRANTED
            }.let { permissionsDeniedList ->
                if (permissionsDeniedList.isEmpty()) {
                    listener.onPermissionGranted(request.first)
                } else {
                    permissionsDeniedList.find {
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, it).not()
                    }?.let {
                        listener.onPermissionNeverAskAgain(request.first)
                    } ?: run {
                        listener.onPermissionDenied(request.first)
                    }
                }
            }
        }
    }

    fun openPermissions() {
        try {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", activity.packageName, null)
                activity.startActivityForResult(this, PERMISSION_REQUEST_CODE)
            }
        } catch (e: Exception) {
        }
    }
}

fun hasPermission(context: Context, permissions: List<String>): Boolean {
    return permissions.map {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }.find {
        !it
    } ?: true
}