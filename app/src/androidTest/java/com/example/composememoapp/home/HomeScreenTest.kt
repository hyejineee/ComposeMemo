package com.example.composememoapp.home

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.home.HomeScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var context: Context

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.mainClock.advanceTimeBy(50L)
    }

    private fun setContentWithHomeScreen() {
        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                HomeScreen()
            }
        }
    }

    @Test
    fun 홈_타이틀을_보여준다() {
        setContentWithHomeScreen()
        composeTestRule
            .onNodeWithText(context.getString(R.string.homeTitle))
            .assertIsDisplayed()
    }

    @Test
    fun 메모_검색창을_보여준다() {
        setContentWithHomeScreen()
        composeTestRule
            .onNodeWithText(context.getString(R.string.putSearchWordCaption))
            .assertIsDisplayed()
    }

    @Test
    fun 카테고리_선택_메뉴를_보여준다() {
        setContentWithHomeScreen()
        composeTestRule
            .onAllNodes(hasScrollAction())[0]
            .assertIsDisplayed()
    }

    @Test
    fun 저장된_메모를_보여준다() {
    }

    @Test
    fun 메모_추가_버튼을_누르면_메모_작성_화면으로_이동한다() {
    }
}
