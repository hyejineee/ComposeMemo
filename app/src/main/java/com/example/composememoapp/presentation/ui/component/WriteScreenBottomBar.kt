package com.example.composememoapp.presentation.ui.component

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.util.toPx

@Composable
fun WriteScreenBottomBar(
    handleClickAddImageButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
        modifier
            .fillMaxWidth()
    ) {

        val addImageIconModel =
            MiniFloatingButtonModel(
                icon = ImageVector.vectorResource(id = R.drawable.ic_round_image_24),
                description = "add image icon",
                onClick = handleClickAddImageButton
            )

        MiniFloatingButton(
            model = addImageIconModel,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp, bottom = 16.dp, top = 16.dp)
                .size(35.dp),
            tint = MaterialTheme.colors.primary
        )
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun WriteScreenBottomBarPreview() {
    ComposeMemoAppTheme {
        WriteScreenBottomBar(
            handleClickAddImageButton = {}
        )
    }
}
