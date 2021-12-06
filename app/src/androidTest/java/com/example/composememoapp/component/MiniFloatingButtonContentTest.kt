package com.example.composememoapp.component

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.MiniFloatingButtonContent
import com.example.composememoapp.presentation.ui.component.MiniFloatingButtonModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExperimentalAnimationApi
@RunWith(AndroidJUnit4::class)
class MiniFloatingButtonContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val onClickMock = mock<()->Unit>()
    private val model = MiniFloatingButtonModel(
        icon = Icons.Default.Call,
        onClick = onClickMock,
        description = "call icon"
    )

    private fun setContentWithMiniFloatingButtonContent(
        model: MiniFloatingButtonModel,
        isFirstItem: Boolean
    ) {
        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                MiniFloatingButtonContent(model = model, isFirstItem = isFirstItem)
            }
        }
    }

    @Test
    fun haveIcon() {
        setContentWithMiniFloatingButtonContent(model = model, isFirstItem = false)
        composeTestRule
            .onNodeWithContentDescription(model.description ?: "")
            .assertIsDisplayed()
    }

    @Test
    fun onClickIsCalledWhenIsFirstItemFalse() {
        setContentWithMiniFloatingButtonContent(model = model, isFirstItem = false)
        composeTestRule
            .onNodeWithContentDescription(model.description ?: "")
            .performClick()
        verify(onClickMock, times(1)).invoke()
    }

    @Test
    fun onClickIsNotCalledWhenIsFirstItemTrue() {
        setContentWithMiniFloatingButtonContent(model = model, isFirstItem = true)
        composeTestRule
            .onNodeWithContentDescription(model.description ?: "")
            .performClick()
        verify(onClickMock, times(0)).invoke()
    }
}
