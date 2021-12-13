package com.example.composememoapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.data.entity.MemoEntity

class MemoViewModel : ViewModel() {

    fun getMemo(memoId: Int): MemoEntity {
        return MemoEntity(
            id = 0,
            contents = List(10) {
                TextBlock(seq = it, content = "this is text block content $it")
            }
        )
    }
}
