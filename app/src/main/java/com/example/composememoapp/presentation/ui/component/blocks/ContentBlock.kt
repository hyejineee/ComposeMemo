package com.example.composememoapp.presentation.ui.component.blocks

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composememoapp.data.database.entity.ContentBlockEntity

interface ContentBlock<T> {
    var seq: Long
    var content: T

    @Composable
    fun drawOnlyReadContent(modifier: Modifier)

    @Composable
    fun drawEditableContent(modifier: Modifier)

    fun convertToContentBlockEntity(): ContentBlockEntity
}
