package com.example.droidrenderdemoearth

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStreamReader

object FileUtils {

    fun getNumberOfBytesFromAsset(context: Context, fileName: String): Int {
        try {
            val inputStream = context.assets.open(fileName)
            val numberOfBytes = inputStream.available()
            inputStream.close() // Close the stream after getting the length
            return numberOfBytes
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return 0
    }

    fun readFileFromAsset(context: Context, fileName: String): ByteArray? {
        val numberOfBytes = getNumberOfBytesFromAsset(context, fileName)
        if (numberOfBytes > 0) {
            val inputStream = context.assets.open(fileName)
            val buffer = ByteArray(numberOfBytes)
            try {
                // Read all bytes into the buffer
                inputStream.read(buffer)
                return buffer
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    fun readFileFromAssetAsString(context: Context, fileName: String): String? {
        return readFileFromAsset(context, fileName)?.let {
            String(it)
        }
    }

    fun readFileFromAssetAsBitmap(context: Context, fileName: String): Bitmap? {
        return readFileFromAsset(context, fileName)?.let {
            //String(it)

            BitmapFactory.decodeByteArray(it, 0, it.size)

        }
    }



}