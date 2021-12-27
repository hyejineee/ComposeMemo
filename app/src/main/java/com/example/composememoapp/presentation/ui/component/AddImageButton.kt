package com.example.composememoapp.presentation.ui.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.composememoapp.util.Permissions
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun AddImageButton(
    modifier: Modifier = Modifier,
    handleAddImage: (Uri?) -> Unit
) {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageSelectDialog by remember { mutableStateOf(false) }

    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()

    val context = LocalContext.current

    val getImageFromGalleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imageUri = uri
                handleAddImage(imageUri)
            }
        }

    val getImageFromCameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) {
            if (it) {
                handleAddImage(imageUri)
            }
        }

    val takePicture = {
        val imageName = System.currentTimeMillis().toString() + ".jpeg"
        val dirName = "images"
        val dir = File(context.filesDir, dirName)

        if (dir.exists().not()) {
            dir.mkdirs()
        }

        val file = File(dir, imageName)
        val uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            file
        )

        imageUri = uri
        getImageFromCameraLauncher.launch(uri)
    }

    val permissionDeniedAction = {
        snackScope.launch {
            snackState.showSnackbar(
                message = "카메라 권한이 거부 되었습니다. 사진 찍기 기능을 사용할 수 없습니다. 설정에서 권한을 허용 해주세요.",
                duration = SnackbarDuration.Short
            )
        }
    }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            if (it) takePicture() else permissionDeniedAction()
        }

    val rationaleAction = {
        snackScope.launch {
            val result = snackState.showSnackbar(
                message = "사진 찍기 기능을 위해서 카메라 권한이 허용되어야 합니다.",
                duration = SnackbarDuration.Long,
                actionLabel = "허용"
            )

            when (result) {
                SnackbarResult.ActionPerformed -> {
                    requestPermissionLauncher.launch(Permissions.CAMERA)
                }
                else -> {
                    permissionDeniedAction()
                }
            }
        }
    }

    val handleClickAddImageButton = {
        showImageSelectDialog = showImageSelectDialog.not()
    }

    val addImageIconModel =
        MiniFloatingButtonModel(
            icon = ImageVector.vectorResource(id = R.drawable.ic_round_image_24),
            description = "add image icon",
            onClick = handleClickAddImageButton
        )

    Box() {
        Column(modifier = modifier) {
            if (showImageSelectDialog) {
                DropDownList(
                    modifier = Modifier
                        .widthIn(200.dp, 200.dp)
                        .padding(bottom = 10.dp)
                        .wrapContentWidth(),
                    list = listOf("사진 선택", "사진 찍기"),
                    onClick = {
                        when (it) {
                            "사진 선택" -> {
                                showImageSelectDialog = false
                                getImageFromGalleryLauncher.launch("image/*")
                            }
                            "사진 찍기" -> {
                                showImageSelectDialog = false
                                Permissions.handlePermissionRequest(
                                    permission = Permissions.CAMERA,
                                    context = context,
                                    grantedAction = { takePicture() },
                                    rationaleAction = {
                                        rationaleAction()
                                    },
                                    requestPermissionAction = {
                                        requestPermissionLauncher.launch(Permissions.CAMERA)
                                    }
                                )
                            }
                        }
                    }
                )
            }
            MiniFloatingButton(
                model = addImageIconModel,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(40.dp)
            )
        }

        SnackbarHost(hostState = snackState)
    }
}

@Preview
@Composable
fun AddImageButtonPreview() {
    ComposeMemoAppTheme {
        AddImageButton(handleAddImage = {})
    }
}
