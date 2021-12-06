package com.example.composememoapp.container

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.composememoapp.data.MemoEntity
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.home.MemoList
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class MemoListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var context: Context

    private val onItemClickMock = mock<(MemoEntity)->Unit>()

    private val memoList = List(5) {
        MemoEntity(
            id = it,
            contents = List(5) { s ->
                TextBlock(seq = s, contents = "content$s")
            }
        )
    }
    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.mainClock.advanceTimeBy(50L)
    }

    private fun setContentWithMemoList(
        memos: List<MemoEntity>
    ) {
        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                MemoList(memos = memos, onItemClick = onItemClickMock)
            }
        }
    }

    @Test
    fun showMemos() {
        setContentWithMemoList(memos = memoList)
        composeTestRule
            .onAllNodesWithText(memoList.first().contents.first().contents as String)
            .assertCountEquals(5)
    }

    @Test
    fun whenClickMemoItemOnItemClickIsCalled() {
        setContentWithMemoList(memos = memoList)
        composeTestRule
            .onAllNodesWithText(memoList.first().contents.first().contents as String)[0]
            .performClick()

        verify(onItemClickMock).invoke(memoList.first())
    }
}
