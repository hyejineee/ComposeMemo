package com.example.composememoapp.data

import androidx.compose.runtime.Composable

interface ContentBlock<T> {
    var seq: Int
    var contents: T

    fun drawContent(): (@Composable () -> Unit)
}
