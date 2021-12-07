package com.example.composememoapp.container

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
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
        onClick: (String) -> Unit,
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
    fun 카테고리를_클릭하면_onClick을_호출한다() {
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
    fun 카테고리를_클릭하면_selected_값이_변경된다() {

        composeTestRule.mainClock.autoAdvance = false

        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                var selected by remember { mutableStateOf("ALL") }
                val listState = rememberLazyListState()

                CategoryMenuBar(
                    categories = categories,
                    onClick = { selected = it },
                    listState = listState,
                    selected = selected
                )
            }
        }

        composeTestRule
            .onNodeWithText(categories[1])
            .performClick()

        composeTestRule.mainClock.advanceTimeBy(50L)

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("categoryBarTest")
        composeTestRule
            .onNodeWithText(categories[1])
            .assert(isSelected())
    }
}
