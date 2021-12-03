package com.example.composememoapp.presentation.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme

@ExperimentalAnimationApi
@Composable
fun MiniFloatingButtonGroupController(
    firstModel: MiniFloatingButtonModel,
    models: List<MiniFloatingButtonModel>
) {
    var extended by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    MiniFloatingButtonGroup(
        extended = extended,
        firstModel = firstModel,
        models = models,
        modifier = Modifier
            .clickable(interactionSource = interactionSource, indication = null) {
                extended = !extended
            }
            .padding(vertical = 10.dp, horizontal = 20.dp)
    )
}

@ExperimentalAnimationApi
@Composable
fun MiniFloatingButtonGroup(
    extended: Boolean = false,
    modifier: Modifier = Modifier,
    models: List<MiniFloatingButtonModel>,
    firstModel: MiniFloatingButtonModel
) {

    androidx.compose.material.Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50.dp)
    ) {
        Row() {
            MiniFloatingButtonContent(model = firstModel, isFirstItem = true)

            AnimatedVisibility(visible = extended) {
                Row() {
                    models.forEach {
                        MiniFloatingButtonContent(model = it)
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
