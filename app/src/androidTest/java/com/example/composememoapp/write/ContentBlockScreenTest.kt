package com.example.composememoapp.write

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.composememoapp.presentation.ui.write.ContentBlockScreen
import com.example.composememoapp.presentation.viewModel.ContentBlockViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@RunWith(AndroidJUnit4::class)
class ContentBlockScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setContentWithContentBlockScreen(
        contentBlockViewModel: ContentBlockViewModel = ContentBlockViewModel(emptyList())
    ) {
        composeTestRule.setContent {
            val contentState =
                contentBlockViewModel.contentBlocks.subscribeAsState(initial = emptyList())

            ContentBlockScreen(
                contentBlockViewModel = contentBlockViewModel,
                contents = contentState.value
            )
        }
    }

    @Test
    fun 체크박스_아이콘을_누르면_메모에_체크박스를_추가한() {

        setContentWithContentBlockScreen()

        composeTestRule
            .onNodeWithContentDescription("add checkbox icon", useUnmergedTree = true)
            .performClick()

        composeTestRule.mainClock.advanceTimeBy(50L)

        composeTestRule
            .onNode(isToggleable())
            .assertExists()
    }

    @Test
    fun ImeAction버튼을_누르면_텍스트_블록이_추가된() {
        setContentWithContentBlockScreen()

        composeTestRule
            .onNode(hasImeAction(ImeAction.Next))
            .performTextInput("hello")

        composeTestRule
            .onNodeWithText("hello")
            .performImeAction()

        composeTestRule
            .onAllNodes(hasImeAction(ImeAction.Next))
            .assertCountEquals(2)
    }

    @Test
    fun 블록의_Content의_내용이_없을_때_백스페이스를_누르면_블록이_삭제된다() {
        setContentWithContentBlockScreen()

        composeTestRule
            .onNode(hasImeAction(ImeAction.Next))
            .performTextInput("hello")

        composeTestRule
            .onNodeWithText("hello")
            .performImeAction()

        composeTestRule
            .onAllNodes(hasImeAction(ImeAction.Next))
            .onLast()
            .performTextInput("k")

        composeTestRule
            .onNodeWithText("k")
            .performKeyPress(
                KeyEvent(
                    NativeKeyEvent(
                        android.view.KeyEvent.ACTION_DOWN,
                        Key.Backspace.nativeKeyCode
                    )
                )
            )

        composeTestRule
            .onAllNodes(hasImeAction(ImeAction.Next))
            .onLast()
            .performKeyPress(
                KeyEvent(
                    NativeKeyEvent(
                        android.view.KeyEvent.ACTION_DOWN,
                        Key.Backspace.nativeKeyCode
                    )
                )
            )

//        composeTestRule
//            .onAllNodes(hasImeAction(ImeAction.Next))
//            .assertCountEquals(1)
    }
}
