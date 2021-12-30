package com.example.composememoapp.presentation.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
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
import com.example.composememoapp.data.database.entity.TagEntity
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CategoryMenuBar(
    categories: List<TagEntity>,
    onClick: (TagEntity) -> Unit,
    prefix: String? = null,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    selected: TagEntity
) {
    val scope = rememberCoroutineScope()

    scope.launch {
        listState.animateScrollToItem(categories.indexOf(selected))
    }

    LazyRow(modifier = modifier, state = listState) {
        items(categories) { category ->
            CategoryMenuRow(
                category.tag!!,
                modifier = Modifier.selectable(category.tag == selected.tag) {
                    onClick(category)
                },
                isSelected = category.tag == selected.tag,
                prefix = prefix
            )
        }
    }
}

@Composable
fun CategoryMenuRow(
    category: String,
    prefix: String? = null,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val text = prefix?.let {
        "$it$category"
    } ?: "$category"

    Text(
        text = text,
        modifier = modifier.padding(horizontal = 15.dp, vertical = 5.dp),
        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
        fontStyle = FontStyle.Italic,
        fontSize = 15.sp,
        color = if (isSelected) MaterialTheme.colors.secondary else Color.LightGray
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryMenuBarPreview() {
    val categories = List(5) {
        TagEntity(tag = "tag$it")
    }

    ComposeMemoAppTheme() {
        CategoryMenuBar(categories = categories, {}, listState = rememberLazyListState(), selected = categories.first())
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryMenuRowPreview() {
    ComposeMemoAppTheme() {
        CategoryMenuRow(category = "ALL")
    }
}
