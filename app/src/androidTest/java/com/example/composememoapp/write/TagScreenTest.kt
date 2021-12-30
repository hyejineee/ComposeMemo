package com.example.composememoapp.write

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.write.TagScreen
import com.example.composememoapp.util.Descriptions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@RunWith(AndroidJUnit4::class)
class TagScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val tagListMock = listOf("hello", "hi")
    private val allTagMock = listOf("hello", "hi", "memo", "sss")

    @Before
    fun init() {
    }

    private fun setContentWithTagScreen(
        _tagList: List<String>,
        allTag: List<String>,
    ) {

        composeTestRule.setContent {
            ComposeMemoAppTheme() {

                val tagList = remember { mutableStateOf(_tagList) }
                val handleClickAddTag = { tag: String ->
                    tagList.value = _tagList.plus(tag)
                }

                TagScreen(
                    tagList = tagList.value,
                    allTag = allTag,
                    handleClickAddTag = handleClickAddTag
                )
            }
        }
    }

    @Test
    fun 등록된_모든_태그를_보여준다() {
        setContentWithTagScreen(
            _tagList = tagListMock,
            allTag = allTagMock,
        )

        tagListMock.forEach {
            composeTestRule
                .onNodeWithText(it, true)
                .assertIsDisplayed()
        }
    }

    @Test
    fun 태그를_입력하고_추가할_수_있다() {
        setContentWithTagScreen(
            _tagList = tagListMock,
            allTag = allTagMock
        )

        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("new")

        composeTestRule
            .onNodeWithContentDescription(Descriptions.AddTagIcon.text)
            .performClick()

        composeTestRule
            .onNodeWithText("new", true)
            .assertIsDisplayed()
    }

    @Test
    fun 추천_태그_목록의_아이템을_선택하면_해당_태그가_추가된다() {

        setContentWithTagScreen(
            _tagList = tagListMock,
            allTag = allTagMock
        )

        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("m")

        composeTestRule
            .onNodeWithText("memo")
            .performClick()

        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.mainClock.advanceTimeBy(50)

        composeTestRule
            .onNodeWithText("memo", true)
            .assertIsDisplayed()
    }
}
