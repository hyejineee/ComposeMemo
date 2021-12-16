package com.example.composememoapp.presentation.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.data.ContentType
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.StaggeredGridColumn

@Composable
fun MemoList(
    memos: List<MemoEntity>,
    onItemClick: (MemoEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        StaggeredGridColumn(modifier = modifier.padding(horizontal = 20.dp)) {
            for (memo in memos) {
                MemoListItem(
                    memo = memo,
                    modifier = Modifier.clickable { onItemClick(memo) }
                )
            }
        }
    }
}

@Composable
fun MemoListItem(
    memo: MemoEntity,
    modifier: Modifier = Modifier
) {
    androidx.compose.material.Surface(
        elevation = 6.dp,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .heightIn(50.dp, 200.dp)
            .padding(4.dp)

    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            memo.contents.map { it.convertToContentBlockModel() }.forEach {
                it.drawOnlyReadContent(modifier = Modifier)
            }
        }
    }
}



@Preview
@Composable
fun MemoListItemPreview() {
    ComposeMemoAppTheme() {
        val memo = MemoEntity(
            id = 1,
            contents = listOf(
                ContentBlockEntity(type = ContentType.Text, seq = 1L, content = "adskfeiwnocono"),
                ContentBlockEntity(type = ContentType.Text, seq = 2L, content = "adskfeiwnocono"),
            ),
        )
        MemoListItem(memo = memo)
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun MemoListPreview() {
    ComposeMemoAppTheme() {
        val memos = List(10) {
            MemoEntity(
                id = it.toLong(),
                contents = List(5) { seq ->
                    ContentBlockEntity(type = ContentType.Text, seq = seq.toLong(), content = "content $seq")
                }
            )
        }
        MemoList(memos = memos, onItemClick = {})
    }
}
