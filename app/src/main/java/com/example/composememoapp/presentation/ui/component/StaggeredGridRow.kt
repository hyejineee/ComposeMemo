package com.example.composememoapp.presentation.ui.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StaggeredGridRow(
    modifier: Modifier = Modifier,
    itemsSize: Int,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val cellWidths = IntArray(itemsSize) { 0 }
        val cellHeights = IntArray(itemsSize) { 0 }

        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints = constraints)

            cellWidths[index] = placeable.width
            cellHeights[index] = placeable.height

            placeable
        }

        val cellX = IntArray(itemsSize) { 0 }
        val cellY = IntArray(itemsSize) { 0 }

        // 뷰 시작의 x좌표 계산하기
        for (i in 1 until itemsSize) {
            val x = cellX[i - 1] + cellWidths[i - 1]
            if (x >= constraints.maxWidth - cellWidths[i - 1]) {
                cellX[i] = 0
            } else {
                cellX[i] = x
            }
        }

        // 뷰 시작의 y좌표 계산하기
        var y = 0
        placeables.forEachIndexed { index, placeable ->
            if (index > 0 && cellX[index] == 0) {
                y += placeable.height
            }
            cellY[index] = y
        }

        val w = constraints.maxWidth
        val h = ((cellHeights.maxOrNull() ?: 0) + (cellY.maxOrNull() ?: 0))
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // 위치 정하기
        layout(w, h) {

            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = cellX[index],
                    y = cellY[index]
                )
            }
        }
    }
}

@Preview
@Composable
fun StaggeredGridRowPreview() {
    val tagList = List(20) {
        "Tag $it"
    }
    StaggeredGridRow(itemsSize = tagList.size) {
        for (tag in tagList) {
            Text(text = tag)
        }
    }
}
