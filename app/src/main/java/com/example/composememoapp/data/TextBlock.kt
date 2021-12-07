package com.example.composememoapp.data

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

data class TextBlock(
    override var seq: Int,
    override var contents: String
) : ContentBlock<String> {
    override fun drawContent() = @Composable {
        Text(text = contents)
    }
}
