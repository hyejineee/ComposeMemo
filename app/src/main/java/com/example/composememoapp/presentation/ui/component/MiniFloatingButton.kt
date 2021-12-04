package com.example.composememoapp.presentation.ui.component

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme

data class MiniFloatingButtonModel(
    val icon: ImageVector,
    val onClick: () -> Unit,
    val description: String?
)

@ExperimentalAnimationApi
@Composable
fun MiniFloatingButton(
    model: MiniFloatingButtonModel,
    modifier: Modifier = Modifier,
) {

    androidx.compose.material.Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50.dp)
    ) {

        MiniFloatingButtonContent(model = model)
    }
}

@Composable
fun MiniFloatingButtonContent(
    isFirstItem: Boolean = false,
    model: MiniFloatingButtonModel,
    modifier: Modifier = Modifier
) {

    val modifier = if (isFirstItem) {
        modifier.padding(10.dp)
    } else {
        modifier
            .padding(5.dp)
            .clickable(onClick = model.onClick)
            .padding(5.dp)
    }

    Icon(
        modifier = modifier,
        imageVector = model.icon,
        contentDescription = model.description
    )
}

@ExperimentalAnimationApi
@Preview
@Composable
fun MiniFloatingButtonPreview() {
    val model =
        MiniFloatingButtonModel(
            icon = Icons.Default.Call,
            onClick = { Log.d("MiniFAP", "click icon :1") },
            description = ""
        )

    ComposeMemoAppTheme {
        MiniFloatingButton(model = model)
    }
}

@Preview
@Composable
fun MiniFloatingButtonGroupItemPreview() {
    ComposeMemoAppTheme() {
        MiniFloatingButtonContent(
            model = MiniFloatingButtonModel(
                icon = Icons.Default.Call,
                onClick = { /*TODO*/ },
                description = ""
            )
        )
    }
}
