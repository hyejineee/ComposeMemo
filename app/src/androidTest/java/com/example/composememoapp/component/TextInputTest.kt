package com.example.composememoapp.component

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.TextInput
import com.example.composememoapp.util.model.IconModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class TextInputTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var context: Context

    private val onValueChangeMock = mock<(s: String) -> Unit>()
    private val onIconClickMock = mock<() -> Unit>()

    private val iconModelMock =
        IconModel(iconId = R.drawable.ic_launcher_background, description = "first icon")

    private val clickableIconModelMock =
        IconModel(
            iconId = R.drawable.ic_launcher_background,
            description = "last icon",
            onClick = onIconClickMock
        )

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    private fun setContentWithTextInput(
        iconModel: IconModel? = null,
        clickableIconModel: IconModel? = null,
        text: String,
        onValueChange: (s: String) -> Unit
    ) {
        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                TextInput(
                    iconModel = iconModel,
                    clickableIconModel = clickableIconModel,
                    text = text,
                    onValueChange = onValueChange
                )
            }
        }
    }

    @Test
    fun onlyTextField() {
        setContentWithTextInput(text = "hello", onValueChange = onValueChangeMock)

        composeTestRule
            .onNodeWithText("hello")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(iconModelMock.description)
            .assertDoesNotExist()
    }

    @Test
    fun performInputTextCalledOnValueChange() {
        setContentWithTextInput(text = "hello", onValueChange = onValueChangeMock)

        composeTestRule
            .onNodeWithText("hello")
            .performTextInput(" memos")

        verify(onValueChangeMock).invoke("hello memos")
    }

    @Test
    fun withIcon() {
        setContentWithTextInput(
            text = "hello", onValueChange = onValueChangeMock,
            iconModel = iconModelMock
        )

        composeTestRule
            .onNodeWithContentDescription(iconModelMock.description)
            .assertIsDisplayed()
    }

    @Test
    fun withClickableIcon() {
        setContentWithTextInput(
            text = "hello", onValueChange = onValueChangeMock,
            clickableIconModel = clickableIconModelMock
        )

        composeTestRule
            .onNodeWithContentDescription(clickableIconModelMock.description)
            .assertIsDisplayed()
    }

    @Test
    fun whenClickableIconHasOnClick() {
        setContentWithTextInput(
            text = "hello", onValueChange = onValueChangeMock,
            clickableIconModel = clickableIconModelMock
        )

        composeTestRule
            .onNodeWithContentDescription(clickableIconModelMock.description)
            .performClick()

        verify(onIconClickMock).invoke()
    }

    @Test
    fun whenClickableIconNotHasOnClick() {

        val mock = clickableIconModelMock.copy(onClick = null)
        setContentWithTextInput(
            text = "hello", onValueChange = onValueChangeMock,
            clickableIconModel = mock
        )

        composeTestRule
            .onNodeWithContentDescription(clickableIconModelMock.description)
            .performClick()

        verify(onIconClickMock, times(0)).invoke()
    }
}
