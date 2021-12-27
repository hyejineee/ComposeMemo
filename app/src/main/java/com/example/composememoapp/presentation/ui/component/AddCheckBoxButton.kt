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
fun AddCheckBoxButton(
    modifier: Modifier = Modifier,
    handleAddCheckBox: () -> Unit
) {

    Box() {
        val addCheckIconModel = MiniFloatingButtonModel(
            icon = ImageVector.vectorResource(id = R.drawable.ic_round_check_circle_24),
            description = "add checkbox icon",
            onClick = handleAddCheckBox
        )

        MiniFloatingButton(
            model = addCheckIconModel,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Preview
@Composable
fun AddCheckBoxButtonPreview() {
    ComposeMemoAppTheme {
        AddCheckBoxButton(
            handleAddCheckBox = {}
        )
    }
}
