package com.example.composememoapp.detailandwrite

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.detailandwrite.DetailAndWriteScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalComposeUiApi
@RunWith(AndroidJUnit4::class)
class DetailAndWriteScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val memo = MemoEntity(
        id = 0,
        contents = List(10) {
            TextBlock(seq = it, content = "this is text block content $it")
        }
    )

    private fun setContentWithDetailAndWriteScreen(
        memoEntity: MemoEntity? = null
    ) {

        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                DetailAndWriteScreen(
                    memoEntity = memoEntity,
                    {}
                )
            }
        }
    }

    @Test
    fun 메모_상세보기_모드일때_메모의_내용을_보여준다() {

        setContentWithDetailAndWriteScreen(
            memoEntity = memo,
        )

        composeTestRule
            .onNodeWithText(memo.contents.first().content as String)
            .assertIsDisplayed()
    }

    @Test
    fun 새로운_메모를_작성하는_모드일_때_화면을_클릭하면_바로_메로를_작성할_수_있다() {
        setContentWithDetailAndWriteScreen()

        composeTestRule
            .onNode(hasAnyChild(hasSetTextAction()))
            .performClick()

        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("hello")

        composeTestRule
            .onNode(hasSetTextAction())
            .assertTextEquals("hello")
    }
}
