package com.example.composememoapp.home

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.home.SearchMemoTextInput
import com.example.composememoapp.util.Descriptions
import com.example.composememoapp.util.model.TextInputSate
import com.example.composememoapp.util.model.rememberTextInputState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchMemoTextInputTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var context: Context

    private val hint by lazy { context.getString(R.string.putSearchWordCaption) }
    private val textInputStateMock = TextInputSate("")

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    private fun setContentWithSearchMemoTextInput(
        mockState: TextInputSate? = null,
        modifier: Modifier = Modifier
    ) {
        composeTestRule.setContent {

            ComposeMemoAppTheme {
                mockState?.let {
                    SearchMemoTextInput(state = it)
                } ?: run {
                    val state = rememberTextInputState(initialText = "")
                    SearchMemoTextInput(state = state)
                }
            }
        }
    }

    @Test
    fun 값이_없는_경우_힌트를_보여준다() {
        setContentWithSearchMemoTextInput(
            modifier = Modifier.padding(10.dp)
        )

        composeTestRule
            .onNodeWithText(hint)
            .assertIsDisplayed()
    }

    @Test
    fun 아이콘을_보여준다() {
        setContentWithSearchMemoTextInput()

        composeTestRule
            .onNodeWithContentDescription(Descriptions.SearchIcon.text)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(Descriptions.ClearIcon.text)
            .assertIsDisplayed()
    }

    @Test
    fun 클리어_아이콘을_클릭했을_때_입력된_텍스트를_지원준다() {
        setContentWithSearchMemoTextInput()

        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("hello")

        composeTestRule
            .onNodeWithText("hello")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(Descriptions.ClearIcon.text)
            .performClick()

        composeTestRule
            .onNodeWithText("hello")
            .assertDoesNotExist()
    }

    @Test
    fun TextState가_변경_되었을_때_TextInput의_값도_변경된다() {
        setContentWithSearchMemoTextInput(
            mockState = textInputStateMock
        )

        textInputStateMock.text = "hello"

        composeTestRule
            .onNodeWithText("hello")
            .assertIsDisplayed()
    }

    @Test
    fun TextInput의_값이_변경되면_TextState의_값도_변경된다() {
        setContentWithSearchMemoTextInput(
            mockState = textInputStateMock
        )

        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("hello")

        assertThat(textInputStateMock.text, equalTo("hello"))
    }

    @Test
    fun 입력된_텍스트_값이_없을때_힌트를_보여준다() {
        setContentWithSearchMemoTextInput()

        composeTestRule
            .onNode(hasSetTextAction())
            .performTextClearance()

        composeTestRule
            .onNodeWithText(hint)
            .assertIsDisplayed()
    }
}
