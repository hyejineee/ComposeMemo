package com.example.composememoapp.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
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
import java.lang.Exception

@kotlinx.parcelize.Parcelize
data class ImageBlock(
    override var seq: Long,
    override var content: Uri?,
) : ContentBlock<Uri?>, Parcelable {

    lateinit var imageState:MutableState<Bitmap?>


    @Composable
    override fun drawOnlyReadContent(modifier:Modifier) {
        val context = LocalContext.current
        imageState = remember{ mutableStateOf<Bitmap?>(null)}
        setImageState(context = context)

        Box(modifier = modifier) {
            imageState.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    @Composable
    override fun drawEditableContent(modifier: Modifier) {

        val context = LocalContext.current

        setImageState(context = context)

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

    private fun setImageState(context:Context){
        content?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                try {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, it)
                    imageState.value = ImageDecoder.decodeBitmap(source)
                }catch (e:Exception){
                    Log.d("ImageBlock", "exception : ${e.message}")
                }

            } else {
                imageState.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            }
        }
    }
}
