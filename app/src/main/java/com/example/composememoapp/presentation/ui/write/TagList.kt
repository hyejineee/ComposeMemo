package com.example.composememoapp.presentation.ui.write

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composememoapp.presentation.ui.component.Chip
import com.example.composememoapp.presentation.ui.component.StaggeredGridRow

@Composable
fun TagList(
    tagList: List<String>
) {

    Row(modifier = Modifier.padding(10.dp)) {
        StaggeredGridRow(
            itemsSize = tagList.size,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            for (tag in tagList) {
                Chip(text = "#$tag")
            }
        }
    }
}
