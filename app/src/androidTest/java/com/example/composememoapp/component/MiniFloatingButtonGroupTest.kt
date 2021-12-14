package com.example.composememoapp.component

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.MiniFloatingButtonGroup
import com.example.composememoapp.presentation.ui.component.MiniFloatingButtonModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock

@ExperimentalAnimationApi
@RunWith(AndroidJUnit4::class)
class MiniFloatingButtonGroupTest {

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

    private fun setContentWithMiniFloatingButtonGroup(
        extended: Boolean,
        models: List<MiniFloatingButtonModel>,
        firstModel: MiniFloatingButtonModel
    ) {
        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                MiniFloatingButtonGroup(
                    extended = extended,
                    models = models,
                    firstModel = firstModel
                )
            }
        }
    }

    @Test
    fun whenExtendedValueFalseShowOneIconButton() {
        setContentWithMiniFloatingButtonGroup(
            extended = false,
            models = models.subList(1, models.size),
            firstModel = models.first()
        )

        composeTestRule
            .onNodeWithContentDescription(models.first().description ?: "")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(models[1].description ?: "")
            .assertDoesNotExist()
    }

    @Test
    fun whenExtendedValueTrueShowAllIconButton() {
        setContentWithMiniFloatingButtonGroup(
            extended = true,
            models = models.subList(1, models.size),
            firstModel = models.first()
        )

        composeTestRule
            .onNodeWithContentDescription(models.first().description ?: "")
            .assertIsDisplayed()

        composeTestRule
            .onAllNodes(hasClickAction())
            .assertCountEquals(4)
    }
}
