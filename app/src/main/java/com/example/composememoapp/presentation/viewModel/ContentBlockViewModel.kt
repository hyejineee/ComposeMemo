package com.example.composememoapp.presentation.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.presentation.ui.component.CheckBoxBlock
import com.example.composememoapp.presentation.ui.component.CheckBoxModel
import com.example.composememoapp.presentation.ui.component.blocks.ContentBlock
import com.example.composememoapp.presentation.ui.component.blocks.ImageBlock
import com.example.composememoapp.presentation.ui.component.blocks.TextBlock
import io.reactivex.rxjava3.subjects.BehaviorSubject

class ContentBlockViewModel(
    initialContentBlock: List<ContentBlockEntity>
) : ViewModel() {

    private val _contentBlocksSource = BehaviorSubject.create<List<ContentBlock<*>>>()
    private var contentBlockList: MutableList<ContentBlock<*>> = mutableListOf()

    val contentBlocks = _contentBlocksSource.compose { it }

    init {
        _contentBlocksSource.onNext(initialContentBlock.map { it.convertToContentBlockModel() })

        if (initialContentBlock.isEmpty()) {
            insertTextBlock()
        }

        contentBlocks.subscribe {
            contentBlockList = it.toMutableList()
        }
    }

    fun insertTextBlock(index: Int? = null) {
        index?.let {
            contentBlockList.add(it, TextBlock(content = ""))
        } ?: contentBlockList.add(TextBlock(content = ""))

        _contentBlocksSource.onNext(contentBlockList.toList())
    }

    fun insertImageBlock(index: Int? = null, uri: Uri) {
        index?.let {
            contentBlockList.add(it, ImageBlock(content = uri))
        } ?: contentBlockList.add(ImageBlock(content = uri))

        _contentBlocksSource.onNext(contentBlockList.toList())
    }

    fun insertCheckBoxBlock(index: Int? = null) {
        index?.let {
            contentBlockList.add(
                it, CheckBoxBlock(content = CheckBoxModel(text = "", false))
            )
        } ?: contentBlockList.add(CheckBoxBlock(content = CheckBoxModel(text = "", false)))

        _contentBlocksSource.onNext(contentBlockList.toList())
    }

    fun deleteBlock(block: ContentBlock<*>) {
        if (contentBlockList.size <= 1) {
            return
        }

        contentBlockList.remove(block)
        _contentBlocksSource.onNext(contentBlockList.toList())
    }
}
