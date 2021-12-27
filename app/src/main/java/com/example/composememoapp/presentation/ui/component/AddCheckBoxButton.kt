package com.example.composememoapp.presentation.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme

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
