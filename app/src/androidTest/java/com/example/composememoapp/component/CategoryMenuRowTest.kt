package com.example.composememoapp.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.home.CategoryMenuRow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryMenuRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setContentWithCategoryMenuRow(
        category: String,
        isSelected: Boolean = false
    ) {
        composeTestRule.setContent {
            ComposeMemoAppTheme {
                CategoryMenuRow(
                    category = category,
                    isSelected = isSelected,
                )
            }
        }
    }

    @Test
    fun 카테고리_이름을_보여준다() {
        setContentWithCategoryMenuRow("ALL")

        composeTestRule.onNodeWithText("ALL").assertIsDisplayed()
    }

    @Test
    fun whenIsSelectedTrueTextColorChanged() {
        setContentWithCategoryMenuRow("ALL", isSelected = true)

        composeTestRule
            .onNodeWithText("ALL")
            .assertIsDisplayed()
    }
}
