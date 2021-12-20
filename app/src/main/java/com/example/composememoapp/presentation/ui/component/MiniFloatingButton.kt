package com.example.composememoapp.presentation.ui.component

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme

data class MiniFloatingButtonModel(
    val icon: ImageVector,
    val onClick: () -> Unit,
    val description: String?
)

@Composable
fun MiniFloatingButton(
    model: MiniFloatingButtonModel,
    tint : Color = MaterialTheme.colors.onPrimary,
    modifier: Modifier = Modifier,
) {

    androidx.compose.material.Surface(
        elevation = 6.dp,
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
    ) {

        MiniFloatingButtonContent(model = model, tint = tint)
    }
}

@Composable
fun MiniFloatingButtonContent(
    isFirstItem: Boolean = false,
    model: MiniFloatingButtonModel,
    tint : Color = MaterialTheme.colors.onPrimary,
    modifier: Modifier = Modifier
) {

    val modifier = if (isFirstItem) {
        modifier.padding(5.dp)
    } else {
        modifier
            .padding(2.dp)
            .clickable(onClick = model.onClick)
            .padding(2.dp)
    }

    Icon(
        modifier = modifier,
        imageVector = model.icon,
        contentDescription = model.description,
        tint = tint
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
