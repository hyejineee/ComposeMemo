package com.example.composememoapp.presentation.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme

@ExperimentalAnimationApi
@Composable
fun MiniFloatingButtonGroup(
    extended: Boolean = false,
    modifier: Modifier = Modifier,
    models: List<MiniFloatingButtonModel>,
    firstModel: MiniFloatingButtonModel,
    tint: Color = MaterialTheme.colors.primary,
) {

    androidx.compose.material.Surface(
        elevation = 6.dp,
        modifier = modifier,
        shape = RoundedCornerShape(50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            MiniFloatingButtonContent(model = firstModel, isFirstItem = true, tint = tint)

            AnimatedVisibility(visible = extended) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    models.forEach {
                        MiniFloatingButtonContent(model = it, tint = tint)
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun MiniFloatingButtonGroupExtendedPreview() {
    val buttons = List(5) {
        MiniFloatingButtonModel(icon = Icons.Default.Call, onClick = {}, description = "")
    }
    ComposeMemoAppTheme {
        MiniFloatingButtonGroup(extended = true, firstModel = buttons.first(), models = buttons)
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun MiniFloatingButtonGroupNotExtendedPreview() {
    val buttons = List(5) {
        MiniFloatingButtonModel(icon = Icons.Default.Call, onClick = {}, description = "")
    }
    ComposeMemoAppTheme {
        MiniFloatingButtonGroup(firstModel = buttons.first(), models = buttons)
    }
}
