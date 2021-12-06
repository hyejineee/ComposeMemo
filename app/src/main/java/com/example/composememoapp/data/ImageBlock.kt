package com.example.composememoapp.data

import androidx.compose.runtime.Composable

data class ImageBlock(
    override var seq: Int,
    override var contents: String
) : ContentBlock<String> {
    override fun drawContent() = @Composable {
    }
}
