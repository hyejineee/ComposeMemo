package com.example.composememoapp.container

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
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
    }

    private fun setContentWithHomeScreen() {
        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                HomeScreen()
            }
        }
    }

    @Test
    fun hasMemoTitle() {
        setContentWithHomeScreen()
        composeTestRule
            .onNodeWithText(context.getString(R.string.homeTitle))
            .assertIsDisplayed()
    }

    @Test
    fun hasSearchInput() {
        setContentWithHomeScreen()
        composeTestRule
            .onNodeWithText(context.getString(R.string.putSearchWordCaption))
            .assertIsDisplayed()
    }
}
