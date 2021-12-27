package com.example.composememoapp.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DropDownList(
    modifier: Modifier = Modifier,
    list: List<String> = emptyList(),
    onClick: (String) -> Unit = {}
) {
    androidx.compose.material.Surface(
        elevation = 2.dp,
        shape = RoundedCornerShape(5.dp),
        modifier = modifier

    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            list.forEach {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(3.dp)
                        .clickable(onClick = { onClick(it) })
                        .fillMaxWidth()
                )
            }
        }
    }
}
