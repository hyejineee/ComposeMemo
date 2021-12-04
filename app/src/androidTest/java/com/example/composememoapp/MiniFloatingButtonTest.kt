package com.example.composememoapp

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.MiniFloatingButton
import com.example.composememoapp.presentation.ui.component.MiniFloatingButtonModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalAnimationApi
@RunWith(AndroidJUnit4::class)
class MiniFloatingButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val onClickMock = mock<()->Unit>()
    private val model = MiniFloatingButtonModel(
        icon = Icons.Default.Call,
        onClick = onClickMock,
        description = "call icon"
    )

    private fun setContentWithMiniFloatingButton(
        model: MiniFloatingButtonModel,
    ) {
        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                MiniFloatingButton(model = model)
            }
        }
    }

    @Test
    fun haveIcon() {
        setContentWithMiniFloatingButton(model = model)

        composeTestRule
            .onNodeWithContentDescription(model.description ?: "")
            .assertIsDisplayed()
    }

    @Test
    fun onClickIsCalled() {
        setContentWithMiniFloatingButton(model = model)
        composeTestRule
            .onNodeWithContentDescription(model.description ?: "")
            .performClick()
        verify(onClickMock).invoke()
    }
}
