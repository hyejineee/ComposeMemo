package com.example.composememoapp

import android.content.Context
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.composememoapp.ui.theme.ComposeMemoAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MemoAppComposableTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var context:Context

    @Before
    fun init(){
        context = InstrumentationRegistry.getInstrumentation().targetContext
        composeTestRule.setContent {
            ComposeMemoAppTheme {
                MemoApp()
            }
        }
    }

    @Test
    fun testMemoAppComposableHaveNavHostComposable(){
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.navHostComposableDescription))
            .assertExists()
    }
}