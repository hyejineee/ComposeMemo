package com.example.composememoapp.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.composememoapp.util.toPx

@Composable
fun BottomBar(
    handleClickAddMemoButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0x00000000), Color.White),
                    startY = 0.dp.toPx(),
                    endY = 100.dp.toPx()
                )
            )
    ) {
        FloatingActionButton(
            backgroundColor = MaterialTheme.colors.primaryVariant,
            onClick = handleClickAddMemoButton,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add memo")
        }
    }
}