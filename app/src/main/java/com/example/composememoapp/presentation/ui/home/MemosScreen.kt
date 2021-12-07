package com.example.composememoapp.presentation.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composememoapp.data.MemoEntity

@Composable
fun MemosScreen(
    isGridMode: Boolean = false,
    memos: List<MemoEntity>,
    onItemClick: (MemoEntity) -> Unit,
    modifier: Modifier = Modifier,
) {

    MemoList(memos = memos, onItemClick = onItemClick, modifier = modifier)
}
