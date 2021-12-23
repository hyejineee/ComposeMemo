package com.example.composememoapp.data

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.presentation.ui.component.TextInput

@kotlinx.parcelize.Parcelize
data class ImageBlock(
    override var seq: Long,
    override var content: Uri?,
) : ContentBlock<Uri?>, Parcelable {

    lateinit var imageState:MutableState<Bitmap?>


    @Composable
    override fun drawOnlyReadContent(modifier:Modifier) {
//        Box(modifier = modifier) {
//            Image(text = content, fontSize = 13.sp)
//        }
    }

    @Composable
    override fun drawEditableContent(modifier: Modifier) {

        val context = LocalContext.current

        content?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder
                    .createSource(context.contentResolver, it)
                imageState.value = ImageDecoder.decodeBitmap(source)
            } else {
                imageState.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            imageState.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    override fun convertToContentBlockEntity() = ContentBlockEntity(
        type = ContentType.Image,
        seq = seq,
        content = content.toString()
    )
}
