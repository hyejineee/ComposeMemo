package com.example.composememoapp

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.MiniFloatingButtonGroup
import com.example.composememoapp.presentation.ui.component.MiniFloatingButtonGroupController
import com.example.composememoapp.presentation.ui.component.MiniFloatingButtonModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock

@ExperimentalAnimationApi
@RunWith(AndroidJUnit4::class)
class MiniFloatingButtonGroupControllerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val onClickMock = mock<() -> Unit>()
    private val models = List(5) {
        MiniFloatingButtonModel(
            icon = Icons.Default.Call,
            onClick = onClickMock,
            description = "call icon $it"
        )
    }

    private fun setContentWithMiniFloatingButtonGroupController(
        models: List<MiniFloatingButtonModel>,
        firstModel: MiniFloatingButtonModel
    ) {
        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                MiniFloatingButtonGroupController(
                    firstModel = firstModel, models = models
                )
            }
        }
    }

    @Test
    fun changeExtendedValueWhenClicked() {
        setContentWithMiniFloatingButtonGroupController(
            models = models.subList(1, models.size),
            firstModel = models.first()
        )

        composeTestRule
            .onNode(hasAnyChild(hasContentDescription(models.first().description ?: "")))
            .performClick()

        composeTestRule
            .onNode(
                hasAnyChild(hasContentDescription(models.first().description ?: "")),
                useUnmergedTree = true
            )
            .onChildren()
            .assertCountEquals(5)
    }
}
