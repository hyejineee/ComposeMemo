package com.example.composememoapp.presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun StaggeredGridColumn(
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