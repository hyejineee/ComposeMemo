package com.example.composememoapp

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.MemoApp
import com.example.composememoapp.util.Descriptions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalAnimationApi
@RunWith(AndroidJUnit4::class)
class MemoAppComposableTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var context: Context

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        composeTestRule.setContent {
            ComposeMemoAppTheme {
                MemoApp()
            }
        }
    }

    @Test
    fun testMemoAppComposableHaveNavHostComposable() {
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("MemoAppComposableTest")
        composeTestRule
            .onNodeWithContentDescription(Descriptions.MemoAppNavHost.name)
            .assertExists()
    }

    @Test
    fun haveAddMemoFAB() {
        composeTestRule
            .onNode(hasContentDescription(context.getString(R.string.addMemo)), useUnmergedTree = true)
            .assertIsDisplayed()
    }
}
