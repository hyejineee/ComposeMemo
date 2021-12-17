package com.example.composememoapp.presentation.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composememoapp.data.database.entity.MemoEntity

@Composable
fun MemosListScreen(
    memos: List<MemoEntity>,
    onItemClick: (MemoEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    MemoList(memos = memos, onItemClick = onItemClick, modifier = modifier)
}
