package com.example.composememoapp.write

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.write.TagTextInput
import com.example.composememoapp.util.model.TextInputSate
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@RunWith(AndroidJUnit4::class)
class TagTextInputTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var textStateMock: TextInputSate

    lateinit var context: Context

    @Before
    fun init() {
        textStateMock = TextInputSate(initialText = "")
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    private fun setContentWithTagTextInput(
        state: TextInputSate,
        tagList: List<String> = emptyList(),
        handleClickAddTag: (String) -> Unit,
    ) {

        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                TagTextInput(
                    state = state,
                    tagList = tagList,
                    handleClickAddTag = handleClickAddTag
                )
            }
        }
    }

    @Test
    fun 태그를_입력할_수_있다() {
        setContentWithTagTextInput(
            state = textStateMock,
            handleClickAddTag = {}
        )

        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("hello")

        composeTestRule
            .onNode(hasSetTextAction())
            .assertTextEquals("hello")
    }

    @Test
    fun 빈_태그_입력시_경고_문자가_띄어진다() {

        setContentWithTagTextInput(
            state = textStateMock,
            handleClickAddTag = {}
        )

        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("")

        composeTestRule
            .onNodeWithText(context.resources.getString(R.string.blankTagCaution))
            .assertIsDisplayed()
    }

    @Test
    fun 특수문자_입력시_경고_문자가_띄어진다() {

        setContentWithTagTextInput(
            state = textStateMock,
            handleClickAddTag = {}
        )

        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("@#$#&$(!$)(#*&(#$@")

        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.mainClock.advanceTimeBy(100L)

        composeTestRule
            .onNodeWithText(context.resources.getString(R.string.specialCharacterCaution))
            .assertIsDisplayed()
    }

    @Test
    fun 입력한_글자와_맞는_태그가_있으면_목록으로_보여준다() {

        setContentWithTagTextInput(
            state = textStateMock,
            tagList = listOf("hello", "hi"),
            handleClickAddTag = {}
        )

        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("h")

        composeTestRule
            .onNodeWithText("hello")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("hi")
            .assertIsDisplayed()
    }
}
