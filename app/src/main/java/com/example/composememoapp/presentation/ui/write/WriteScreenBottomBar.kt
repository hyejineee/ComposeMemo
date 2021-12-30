package com.example.composememoapp.presentation.ui.component

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.write.AddCheckBoxButton

@ExperimentalAnimationApi
@Composable
fun WriteScreenBottomBar(
    handleAddImage: (Uri?) -> Unit,
    handleAddCheckBox: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {

            AddImageButton(
                handleAddImage = handleAddImage,
            )

            Spacer(modifier = Modifier.width(10.dp))

            AddCheckBoxButton(
                handleAddCheckBox = handleAddCheckBox,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun WriteScreenBottomBarPreview() {
    ComposeMemoAppTheme {
        WriteScreenBottomBar(
            handleAddImage = {},
            handleAddCheckBox = {}
        )
    }
}
