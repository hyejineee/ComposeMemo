package com.example.composememoapp.container

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.home.CategoryMenuBar
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class CategoryMenuBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val categories = listOf("ALL") + List(10) {
        "#Category$it"
    }

    private val onClickMock = mock<(String) -> Unit>()

    private fun setContentWithCategoryMenuBar(
        categories: List<String>,
        onClick: (selected: String) -> Unit,
        selected: String? = null
    ) {
        composeTestRule.setContent {
            ComposeMemoAppTheme {
                val listState = rememberLazyListState()
                selected?.let {
                    CategoryMenuBar(
                        categories = categories,
                        onClick = onClick,
                        selected = it,
                        listState = listState
                    )
                } ?: kotlin.run {
                    CategoryMenuBar(
                        categories = categories,
                        onClick = onClick,
                        listState = listState
                    )
                }
            }
        }
    }

    @Test
    fun clickCategoryCalledOnClick() {
        composeTestRule.mainClock.autoAdvance = false
        setContentWithCategoryMenuBar(
            categories = categories,
            onClick = onClickMock,
        )

        composeTestRule.mainClock.advanceTimeBy(50L)

        composeTestRule
            .onNodeWithText(categories.first())
            .performClick()

        verify(onClickMock).invoke(categories.first())
    }

    @Test
    fun clickCategorySelectedIsChanged() {

        composeTestRule.mainClock.autoAdvance = false

        setContentWithCategoryMenuBar(
            categories = categories,
            onClick = onClickMock,
            selected = categories[4]
        )

        composeTestRule.mainClock.advanceTimeBy(50L)

        composeTestRule
            .onNodeWithText(categories[4])
            .assertIsDisplayed()
    }
}
