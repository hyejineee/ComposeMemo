package com.example.composememoapp.presentation.ui.detailandwrite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.MiniFloatingButton
import com.example.composememoapp.presentation.ui.component.MiniFloatingButtonModel

@Composable
fun AddCheckBoxButton(
    modifier: Modifier = Modifier,
    handleAddCheckBox: () -> Unit
) {

    Box(modifier = modifier) {
        val addCheckIconModel = MiniFloatingButtonModel(
            icon = ImageVector.vectorResource(id = R.drawable.ic_round_check_circle_24),
            description = "add checkbox icon",
            onClick = handleAddCheckBox
        )

        MiniFloatingButton(
            model = addCheckIconModel,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.fillMaxSize()
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
