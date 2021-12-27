package com.example.composememoapp.presentation.ui.component

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme

@Composable
fun WriteScreenBottomBar(
    handleAddImage: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
    ) {

        AddImageButton(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp, bottom = 16.dp),
            handleAddImage = handleAddImage
        )
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun WriteScreenBottomBarPreview() {
    ComposeMemoAppTheme {
        WriteScreenBottomBar(
            handleAddImage = {}
        )
    }
}
