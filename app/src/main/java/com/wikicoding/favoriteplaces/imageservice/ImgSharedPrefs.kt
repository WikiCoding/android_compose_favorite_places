package com.wikicoding.favoriteplaces.imageservice

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Exception
import java.util.UUID

object ImgSharedPrefs {
    fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(context)

        /** MODE_PRIVATE means that other applications will not be able to access this directory**/
        var file = wrapper.getDir("FavPlacesImages", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    fun convertUriToBitmap(uri: Uri, context: Context): Bitmap? {
        val bitmap: Bitmap
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                return bitmap
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}