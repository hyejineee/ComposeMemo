package com.example.composememoapp.presentation.ui.component.blocks

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.parcelize.IgnoredOnParcel

@kotlinx.parcelize.Parcelize
data class ImageBlock(
    override var seq: Long = 0,
    override var content: Uri?,
) : ContentBlock<Uri?>, Parcelable {

    @IgnoredOnParcel
    var imageState: MutableState<Bitmap?> = mutableStateOf(null)

    @Composable
    override fun drawOnlyReadContent(modifier: Modifier) {

        Box(modifier = modifier.padding(16.dp)) {
            GlideImage(
                imageModel = content,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .aspectRatio(0.8f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
                shimmerParams = ShimmerParams(
                    baseColor = MaterialTheme.colors.background,
                    highlightColor = Color.Gray,
                    durationMillis = 500,
                    dropOff = 0.65f,
                    tilt = 20f
                ),
            )
        }
    }

    @Composable
    override fun drawEditableContent(modifier: Modifier) {

        Box(modifier = modifier.fillMaxWidth()) {
            GlideImage(
                imageModel = content,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .heightIn(50.dp, 500.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop,
                shimmerParams = ShimmerParams(
                    baseColor = MaterialTheme.colors.background,
                    highlightColor = Color.Gray,
                    durationMillis = 500,
                    dropOff = 0.65f,
                    tilt = 20f
                ),
            )
        }
    }

    override fun convertToContentBlockEntity() = ContentBlockEntity(
        type = ContentType.Image,
        seq = seq,
        content = content.toString()
    )
}
