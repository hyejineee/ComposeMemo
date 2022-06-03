package com.example.composememoapp.presentation.ui.component.blocks

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.presentation.viewModel.ContentBlockViewModel

abstract class ContentBlock<T> {

    open val seq: Long by lazy { seq }
    open val content: T by lazy { content }

    @Composable
    abstract fun drawOnlyReadContent(modifier: Modifier)

    abstract fun isEmpty(): Boolean

    abstract fun addNextBlock(viewModel: ContentBlockViewModel)

    @Composable
    abstract fun drawEditableContent(modifier: Modifier, viewModel: ContentBlockViewModel)

    abstract fun convertToContentBlockEntity(): ContentBlockEntity
}
