package com.example.composememoapp.presentation.ui.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import java.io.File

@Composable
fun AddImageButton(
    modifier: Modifier = Modifier,
    handleAddImage: (Uri?) -> Unit
) {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageSelecDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val getImageFromGalleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
            handleAddImage(imageUri)
        }

    val getImageFromCameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()){
            if(it){
                handleAddImage(imageUri)
            }
        }

    val handleClickAddImageButton = {
        showImageSelecDialog = showImageSelecDialog.not()
    }


    val addImageIconModel =
        MiniFloatingButtonModel(
            icon = ImageVector.vectorResource(id = R.drawable.ic_round_image_24),
            description = "add image icon",
            onClick = handleClickAddImageButton
        )

    Column(modifier = modifier) {

        if(showImageSelecDialog){
            DropDownList(
                modifier = Modifier
                    .widthIn(200.dp, 200.dp)
                    .padding(bottom = 10.dp)
                    .wrapContentWidth(),
                list = listOf("사진 선택", "사진 찍기"),
                onClick = {
                    when(it){
                        "사진 선택" -> {
                            getImageFromGalleryLauncher.launch("image/*")
                        }
                        "사진 찍기" ->{
                            val imageName = System.currentTimeMillis().toString() + ".jpeg"
                            val dirName = "images"
                            val dir = File(context.filesDir, dirName)

                            if (dir.exists().not()) {
                                dir.mkdirs()
                            }

                            val file = File(dir, imageName)
                            val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)

                            imageUri = uri
                            getImageFromCameraLauncher.launch(uri)
                        }
                    }
                }
            )
        }

        MiniFloatingButton(
            model = addImageIconModel,

            tint = MaterialTheme.colors.primary
        )
    }
}

@Preview
@Composable
fun AddImageButtonPreview(){
    ComposeMemoAppTheme {
        AddImageButton(handleAddImage = {})
    }
}
