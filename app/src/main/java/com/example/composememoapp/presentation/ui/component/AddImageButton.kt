package com.example.composememoapp.presentation.ui.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
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
