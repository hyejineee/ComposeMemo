package com.example.composememoapp.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.util.model.InputState

interface ContentBlock<T> {
    var seq: Long
    var content: T

    @Composable
    fun drawOnlyReadContent(modifier: Modifier)

    @Composable
    fun drawEditableContent(modifier: Modifier)

    fun convertToContentBlockEntity(): ContentBlockEntity
}
