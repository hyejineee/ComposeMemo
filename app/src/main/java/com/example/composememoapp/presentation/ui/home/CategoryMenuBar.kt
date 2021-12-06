package com.example.composememoapp.presentation.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CategoryMenuBar(
    categories: List<String>,
    onClick: (selected: String) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    selected: String = "ALL"
) {
    val scope = rememberCoroutineScope()

    scope.launch {
        listState.animateScrollToItem(categories.indexOf(selected))
    }

    LazyRow(modifier = modifier, state = listState) {
        items(categories) { category ->
            CategoryMenuRow(
                category,
                modifier = Modifier.clickable(onClick = { onClick(category) }),
                isSelected = category == selected
            )
        }
    }
}

@Composable
fun CategoryMenuRow(
    category: String,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    Text(
        text = category,
        modifier = modifier.padding(horizontal = 15.dp, vertical = 5.dp),
        fontWeight = FontWeight.SemiBold,
        fontStyle = FontStyle.Italic,
        fontSize = 18.sp,
        color = if (isSelected) MaterialTheme.colors.secondary else Color.LightGray
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryMenuBarPreview() {
    val categories = listOf(
        "All",
        "#category1",
        "#category2",
        "#category3",
        "#category4",
        "#category5",
        "#category6"
    )

    ComposeMemoAppTheme() {
        CategoryMenuBar(categories = categories, {}, listState = rememberLazyListState())
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryMenuRowPreview() {
    ComposeMemoAppTheme() {
        CategoryMenuRow(category = "ALL")
    }
}
