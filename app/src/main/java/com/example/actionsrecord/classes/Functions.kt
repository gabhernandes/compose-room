package com.example.actionsrecord.classes

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


var clickedAction: MyAction? = null
var goBack = false
fun uriToByteArray(contentResolver: ContentResolver, uri: Uri): ByteArray? {
    var inputStream: InputStream? = null
    val byteArrayOutputStream = ByteArrayOutputStream()
    try {
        inputStream = contentResolver.openInputStream(uri)
        val buffer = ByteArray(1024)
        var len: Int
        while (inputStream?.read(buffer).also { len = it!! } != -1) {
            byteArrayOutputStream.write(buffer, 0, len)
        }
        return byteArrayOutputStream.toByteArray()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            inputStream?.close()
            byteArrayOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return null
}


fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}


