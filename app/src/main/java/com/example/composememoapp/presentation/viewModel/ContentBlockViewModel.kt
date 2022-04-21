package com.example.composememoapp.presentation.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.presentation.ui.component.CheckBoxBlock
import com.example.composememoapp.presentation.ui.component.CheckBoxModel
import com.example.composememoapp.presentation.ui.component.blocks.ContentBlock
import com.example.composememoapp.presentation.ui.component.blocks.ContentType
import com.example.composememoapp.presentation.ui.component.blocks.ImageBlock
import com.example.composememoapp.presentation.ui.component.blocks.TextBlock
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class ContentBlockViewModel(
    initialContentBlock: List<ContentBlockEntity>
) : ViewModel() {

    private val _contentBlocksSource = BehaviorSubject.create<List<ContentBlock<*>>>()
    private var contentBlockList: MutableList<ContentBlock<*>> = mutableListOf()
    private var focusedIndex: Int = 0

    val contentBlocks: Observable<List<ContentBlock<*>>> = _contentBlocksSource

    init {
        if (initialContentBlock.isEmpty()) {
            insertTextBlock()
        } else {
            _contentBlocksSource.onNext(initialContentBlock.map { it.convertToContentBlockModel() })
            contentBlockList =
                initialContentBlock.map { it.convertToContentBlockModel() }.toMutableList()
        }
    }

    fun insertTextBlock(s: String? = null) {
        contentBlockList.add(TextBlock( content = s ?: ""))
        focusedIndex = contentBlockList.size - 1
        _contentBlocksSource.onNext(contentBlockList.toList())
    }

    fun changeToImageBlock(uri: Uri) {
        contentBlockList.add(focusedIndex + 1, ImageBlock(content = uri))
        insertTextBlock()
    }

    fun changeToCheckBoxBlock() {

        val target = contentBlockList.get(index = focusedIndex).convertToContentBlockEntity()

        if (target.type != ContentType.Text) return

        contentBlockList[focusedIndex] =
            CheckBoxBlock(content = CheckBoxModel(text = target.content, false))

        _contentBlocksSource.onNext(contentBlockList.toList())
    }

    fun deleteBlock(block: ContentBlock<*>) {
        if (contentBlockList.size <= 1) return

        contentBlockList.remove(block)
        _contentBlocksSource.onNext(contentBlockList.toList())
    }

    fun focusedBlock(index: Int) {
        focusedIndex = index
    }
}
