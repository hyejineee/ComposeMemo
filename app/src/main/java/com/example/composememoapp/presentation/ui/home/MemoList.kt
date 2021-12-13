package com.example.composememoapp.presentation.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.data.MemoEntity
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme

@Composable
fun MemoList(
    memos: List<MemoEntity>,
    onItemClick: (MemoEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(memos) { memo ->
            MemoListItem(
                memo = memo,
                onItemClick = onItemClick,
                modifier = Modifier.clickable { onItemClick(memo) }
            )
        }
    }
}

@Composable
fun MemoListItem(
    memo: MemoEntity,
    onItemClick: (MemoEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material.Surface(
        elevation = 6.dp,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)

    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            memo.contents.forEach {
                it.drawOnlyReadContent()
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
                TextBlock(seq = 1, content = "adskfeiwnocono"),
                TextBlock(seq = 1, content = "adskfeiwnocono"),
            ),
        )
        MemoListItem(memo = memo, onItemClick = {})
    }
}

@Preview
@Composable
fun MemoListPreview() {
    ComposeMemoAppTheme() {
        val memos = List(10) {
            MemoEntity(
                id = it,
                contents = List(5) { seq -> TextBlock(seq = seq, content = "content $seq") }
            )
        }
        MemoList(memos = memos, onItemClick = {})
    }
}
