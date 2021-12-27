package com.example.composememoapp.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.util.BitmapProvider
import kotlinx.parcelize.IgnoredOnParcel

@kotlinx.parcelize.Parcelize
data class ImageBlock(
    override var seq: Long,
    override var content: Uri?,
) : ContentBlock<Uri?>, Parcelable {

    @IgnoredOnParcel
    var imageState: MutableState<Bitmap?> = mutableStateOf(null)

    @Composable
    override fun drawOnlyReadContent(modifier: Modifier) {
        getBitmap(LocalContext.current)
        Box(modifier = modifier) {
            imageState.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                    contentScale = ContentScale.FillBounds,
                    colorFilter = ColorFilter.tint(
                        Color(0x70000000),
                        BlendMode.SrcOver
                    )
                )
            }
        }
    }

    @Composable
    override fun drawEditableContent(modifier: Modifier) {
        getBitmap(LocalContext.current)
        Box(modifier = Modifier.fillMaxWidth()) {
            imageState.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }

    override fun convertToContentBlockEntity() = ContentBlockEntity(
        type = ContentType.Image,
        seq = seq,
        content = content.toString()
    )

    private fun getBitmap(context: Context) {
        if (imageState.value == null) {
            val bitmapProvider = BitmapProvider(context = context)
            content?.let { uri ->
                bitmapProvider.getBitmapFromFile(uri)
                    .subscribe(
                        { imageState.value = it },
                        { imageState.value = null }
                    )
            }
        }
    }
}
