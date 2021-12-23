package com.example.composememoapp.presentation.ui.component

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import com.example.composememoapp.R

@Composable
fun AddImageButton(
    modifier: Modifier = Modifier,
    handleClickAddImageButton: (Uri?) -> Unit
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val getImageFromGalleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
            handleClickAddImageButton(imageUri)
        }

    val addImageIconModel =
        MiniFloatingButtonModel(
            icon = ImageVector.vectorResource(id = R.drawable.ic_round_image_24),
            description = "add image icon",
            onClick = {
                getImageFromGalleryLauncher.launch("image/*")
            }
        )

    Row() {
        MiniFloatingButton(
            model = addImageIconModel,
            modifier = modifier,
            tint = MaterialTheme.colors.primary
        )
    }

}