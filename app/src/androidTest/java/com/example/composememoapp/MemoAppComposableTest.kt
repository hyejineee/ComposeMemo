package com.example.composememoapp

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.MemoApp
import com.example.composememoapp.util.Descriptions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
}
