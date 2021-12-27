package com.example.composememoapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.FileNotFoundException

class BitmapProvider(private val context: Context) {

    fun getBitmapFromFile(uri: Uri?): Single<Bitmap> {
        return Single.create<Bitmap?> { observable ->
            uri?.let { uri ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    try {
                        observable.onSuccess(
                            decodeSampledBitmapFromFileInVersionP(
                                uri = uri,
                                context = context,
                                reqHeight = 500,
                                reqWidth = 500
                            )
                        )
                    } catch (e: FileNotFoundException) {
                        observable.onError(e)
                    }
                } else {
                    try {
                        observable.onSuccess(
                            decodeSampledBitmapFromFile(
                                uri = uri,
                                context = context,
                                reqWidth = 500,
                                reqHeight = 500
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

    @RequiresApi(Build.VERSION_CODES.P)
    private fun decodeSampledBitmapFromFileInVersionP(context: Context, uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap {
        val source = ImageDecoder
            .createSource(context.contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, source ->
            var sampleSize = 1
            if (info.size.height > reqHeight || info.size.width > reqWidth) {
                val halfH = info.size.height / 2
                val halfW = info.size.width / 2

                while (halfH / sampleSize >= reqHeight && halfW / sampleSize >= reqWidth) {
                    sampleSize *= 2
                }
            }

            decoder.setTargetSampleSize(sampleSize)
        }
        return bitmap
    }

    private fun decodeSampledBitmapFromFile(
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

                    inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

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
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}
