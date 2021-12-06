package com.example.composememoapp.container

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
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
import com.example.composememoapp.presentation.ui.home.HomeScreen
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

    private val hint = "hint"
    private val textInputStateMock = TextInputSate(hint = hint, initialText = hint)

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    private fun setContentWithSearchMemoTextInput(
        mockState:TextInputSate? = null,
        isHasState:Boolean = true,
        modifier: Modifier = Modifier
    ) {
        composeTestRule.setContent {

            ComposeMemoAppTheme {
                if (isHasState){

                    mockState?.let {
                        SearchMemoTextInput(state = it)
                    }?:run{
                        val state = rememberTextInputState(hint = hint)
                        SearchMemoTextInput(state = state)
                    }
                }else{
                    SearchMemoTextInput(modifier = modifier)
                }
            }
        }
    }

    @Test
    fun initialStateIsShowHint(){
        setContentWithSearchMemoTextInput(
            modifier = Modifier.padding(10.dp)
        )

        composeTestRule
            .onNodeWithText(hint)
            .assertIsDisplayed()
    }

    @Test
    fun stateIsNullDefaultStateIsShown(){
        setContentWithSearchMemoTextInput(isHasState = false)

        composeTestRule
            .onNodeWithText("")
            .assertIsDisplayed()

    }

    @Test
    fun withIconsShowAllIcon(){
        setContentWithSearchMemoTextInput()

        composeTestRule
            .onNodeWithContentDescription(Descriptions.SearchIcon.text)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(Descriptions.ClearIcon.text)
            .assertIsDisplayed()
    }

    @Test
    fun whenClickableIconClickSearchTextIsClear(){
        setContentWithSearchMemoTextInput()

        composeTestRule
            .onNodeWithContentDescription(Descriptions.ClearIcon.text)
            .performClick()

        composeTestRule
            .onNodeWithText(hint)
            .assertDoesNotExist()
    }

    @Test
    fun whenStateIsChangedTextInputValueIsChanged(){
        setContentWithSearchMemoTextInput(
            mockState = textInputStateMock
        )

        textInputStateMock.text = "hello"

        composeTestRule
            .onNodeWithText("hello")
            .assertIsDisplayed()
    }

    @Test
    fun whenValueIsTypedStateValueIsChanged(){
        setContentWithSearchMemoTextInput(
            mockState = textInputStateMock
        )

        composeTestRule
            .onNodeWithText(hint)
            .performTextInput("hello")

        assertThat(textInputStateMock.text, equalTo("hello"))

    }

    @Test
    fun whenTextInputIsBlankShowHint(){
        setContentWithSearchMemoTextInput()

        composeTestRule
            .onNodeWithText(hint)
            .performTextClearance()

        composeTestRule
            .onNodeWithText(hint)
            .assertIsDisplayed()
    }

}
