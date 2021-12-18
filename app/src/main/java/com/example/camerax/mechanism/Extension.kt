package com.example.camerax.mechanism

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.view.View
import android.view.View.*
import android.widget.EditText
import android.widget.Toast
import java.io.ByteArrayOutputStream


fun EditText.get() = text.toString().trim()

fun EditText.validate() = text.toString().isNotEmpty()

fun View.gone() {
    visibility = GONE
}

fun View.visible() {
    visibility = VISIBLE
}

fun View.invisible() {
    visibility = INVISIBLE
}

fun Activity.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun View.toast(message: String) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()

fun toBase64(bm: Bitmap): String? {
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b = baos.toByteArray()
    return encodeToString(b, DEFAULT)
}

fun toBitmap(uri: Uri, context: Context): Bitmap {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
    } else {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }
}
