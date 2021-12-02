package com.example.composememoapp

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.HomeAddMemoFAB
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalAnimationApi
@RunWith(AndroidJUnit4::class)
class HomeAddMemoFABComposableTest {

    companion object {
        const val TAG = "HomeAddMemoFABTest"
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var context: Context

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun whenExtendedValueIsFalseTextIsNotShown() {
        composeTestRule.setContent {
            ComposeMemoAppTheme {
                HomeAddMemoFAB(
                    extended = false,
                    onClick = { /*TODO*/ }
                )
            }
        }

        composeTestRule
            .onNode(hasText(context.getString(R.string.addMemo)))
            .assertDoesNotExist()
    }

    @Test
    fun whenExtendedValueIsTrueTextIsShown() {
        composeTestRule.setContent {
            ComposeMemoAppTheme {
                HomeAddMemoFAB(
                    extended = true,
                    onClick = { /*TODO*/ }
                )
            }
        }

        composeTestRule
            .onNode(hasText(context.getString(R.string.addMemo)))
            .assertIsDisplayed()
    }

    @Test
    fun clickFAB() {
        val onClickMock = mock<()->Unit>()
        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                HomeAddMemoFAB(
                    onClick = onClickMock
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.addMemo))
            .performClick()

        verify(onClickMock).invoke()
    }
}
