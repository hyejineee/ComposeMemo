package com.example.composememoapp.presentation.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme

@Composable
fun Chip(
    text: String
) {
    Surface(
        elevation = 6.dp,
        modifier = Modifier.padding(3.dp),
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Preview
@Composable
fun ChipPreview() {
    ComposeMemoAppTheme() {
        Chip(text = "#hello")
    }
}
