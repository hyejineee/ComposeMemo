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
import com.example.composememoapp.presentation.ui.component.TextInputIconable
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
        showHint: Boolean = false,
        hint: String? = null,
        text: String,
        onValueChange: (s: String) -> Unit
    ) {
        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                if (showHint) {
                    TextInputIconable(
                        iconModel = iconModel,
                        clickableIconModel = clickableIconModel,
                        hint = hint,
                        text = text,
                        onValueChange = onValueChange
                    )
                } else {
                    TextInputIconable(
                        iconModel = iconModel,
                        clickableIconModel = clickableIconModel,
                        text = text,
                        onValueChange = onValueChange
                    )
                }
            }
        }
    }

    @Test
    fun 아이콘이_없는_텍스트_인풋을_그린다() {
        setContentWithTextInput(text = "hello", onValueChange = onValueChangeMock)

        composeTestRule
            .onNodeWithText("hello")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(iconModelMock.description)
            .assertDoesNotExist()
    }

    @Test
    fun 텍스트_입력을_했을_때_onValueChane가_호출된다() {
        setContentWithTextInput(text = "hello", onValueChange = onValueChangeMock)

        composeTestRule
            .onNodeWithText("hello")
            .performTextInput(" memos")

        verify(onValueChangeMock).invoke("hello memos")
    }

    @Test
    fun 아이콘이_있는_텍스트_인풋을_그린다() {
        setContentWithTextInput(
            text = "hello", onValueChange = onValueChangeMock,
            iconModel = iconModelMock
        )

        composeTestRule
            .onNodeWithContentDescription(iconModelMock.description)
            .assertIsDisplayed()
    }

    @Test
    fun 클릭_가능한_아이콘이_있는_텍스트_인풋을_그린다() {
        setContentWithTextInput(
            text = "hello", onValueChange = onValueChangeMock,
            clickableIconModel = clickableIconModelMock
        )

        composeTestRule
            .onNodeWithContentDescription(clickableIconModelMock.description)
            .assertIsDisplayed()
    }

    @Test
    fun onClick메소드가_있는_클릭_가능한_아이콘을_클릭히면_onClick메소드가_호출된() {
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
    fun onClick메소드가_없는_클릭_가능한_아이콘을_클릭하면_메소드가_호출되지_않는다() {

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

    @Test
    fun 힌트가_있는_텍스트_인풋을_그린다() {
        setContentWithTextInput(text = "", onValueChange = onValueChangeMock, hint = "hint", showHint = true)

        composeTestRule
            .onNodeWithText("hint")
            .assertIsDisplayed()
    }

    @Test
    fun 텍스트_인풋에_입력된_값이_없을_경우_힌트를_보여준다() {
        setContentWithTextInput(text = "hello", onValueChange = onValueChangeMock, hint = "hint", showHint = true)

        composeTestRule
            .onNodeWithText("hint")
            .assertDoesNotExist()
    }
}
