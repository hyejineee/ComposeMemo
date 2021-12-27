package com.example.composememoapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.FileNotFoundException

class ImageProvider(private val context: Context) {
    fun getBitmapFromFile(uri: Uri?): Single<Bitmap> {
        return Single.create<Bitmap?> { observable ->
            uri?.let { uri ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    try {
                        val source = ImageDecoder
                            .createSource(context.contentResolver, uri)
                        val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, source ->
                            var sampleSize = 1
                            if (info.size.height > 500 || info.size.width > 500) {
                                val halfH = info.size.height / 2
                                val halfW = info.size.width / 2

                                while (halfH / sampleSize >= 500 && halfW / sampleSize >= 500) {
                                    sampleSize *= 2
                                }
                            }

                            decoder.setTargetSampleSize(sampleSize)
                        }

                        observable.onSuccess(bitmap)
                    } catch (e: Exception) {
                        Log.d("ImageBlock", "exception : ${e.message}")
                        observable.onError(e)
                    }
                } else {
                    try {
                        observable.onSuccess(
                            decodeSampledBitmapFromResource(
                                uri,
                                context,
                                500,
                                500
                            )
                        )
                    } catch (e: FileNotFoundException) {
                        observable.onError(e)
                    }
                }
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun decodeSampledBitmapFromResource(
        uri: Uri?,
        context: Context,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap? {

        uri?.let {
            return BitmapFactory.Options().run {
                try {
                    inJustDecodeBounds = true
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

                    // Calculate inSampleSize
                    inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

                    // Decode bitmap with inSampleSize set
                    inJustDecodeBounds = false

                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } catch (e: FileNotFoundException) {
                    throw e
                }
            }
        }

        return null
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}
