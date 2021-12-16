package com.example.composememoapp.home

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.composememoapp.data.ContentType
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.data.database.entity.MemoEntity
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
            id = it.toLong(),
            contents = List(5) { s ->
                ContentBlockEntity(type = ContentType.Text, seq = s.toLong(), content = "content$s")
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
    fun 메모리스트를_보여준다() {
        setContentWithMemoList(memos = memoList)
        composeTestRule
            .onAllNodesWithText(memoList.first().contents.first().content as String)
            .assertCountEquals(5)
    }

    @Test
    fun 메모_아이템을_클릭하면_onItemClick이_호출된다() {
        setContentWithMemoList(memos = memoList)
        composeTestRule
            .onAllNodesWithText(memoList.first().contents.first().content as String)[0]
            .performClick()

        verify(onItemClickMock).invoke(memoList.first())
    }
}
