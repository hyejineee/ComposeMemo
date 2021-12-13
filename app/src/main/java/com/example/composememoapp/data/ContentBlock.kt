package com.example.composememoapp.data

import androidx.compose.runtime.Composable
import com.example.composememoapp.util.model.InputState

interface ContentBlock<T> {
    var seq: Int
    var content: T

    @Composable
    fun drawOnlyReadContent()

    @Composable
    fun drawEditableContent(state: InputState)
}
