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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.data.entity.MemoEntity
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme

@Composable
fun MemoList(
    memos: List<MemoEntity>,
    onItemClick: (MemoEntity) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        PinterestGrid(modifier = modifier.padding(horizontal = 20.dp)) {
            for (memo in memos) {
                MemoListItem(
                    memo = memo,
                    onItemClick = onItemClick,
                    modifier = Modifier.clickable { onItemClick(memo) }
                )
            }
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
            .heightIn(50.dp, 200.dp)
            .padding(4.dp)

    ) {

        Column(modifier = Modifier.padding(10.dp)) {
            memo.contents.forEach {
                it.drawOnlyReadContent()
            }
        }
    }
}

@Composable
fun PinterestGrid(
    modifier: Modifier = Modifier,
    cols: Int = 2,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val cellWidths = IntArray(cols) { 0 }
        val cellHeights = IntArray(cols) { 0 }

        val placeables = measurables.mapIndexed { index, measurable ->

            val childConstraints = constraints.copy(
                minWidth = constraints.maxWidth / cols,
                maxWidth = constraints.maxWidth / cols
            )
            val placeable = measurable.measure(childConstraints)

            val cell = index % cols
            cellWidths[cell] = constraints.maxWidth / cols
            cellHeights[cell] += placeable.height

            placeable
        }

        val w = constraints.maxWidth
        val h =
            cellHeights.maxOrNull()?.coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))
                ?: constraints.minHeight

        val cellX = IntArray(cols) { 0 }

        for (i in 1 until cols) {
            cellX[i] = cellX[i - 1] + cellWidths[i - 1]
        }

        layout(w, h) {
            val cellY = IntArray(cols) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val cell = index % cols
                placeable.placeRelative(
                    x = cellX[cell],
                    y = cellY[cell],
                )
                cellY[cell] += placeable.height
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

@ExperimentalFoundationApi
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
