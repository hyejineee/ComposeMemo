package com.example.composememoapp.viewModel

import com.example.composememoapp.presentation.viewModel.TagViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TagViewModelTest {

    // 메모를 카테고리에 따라 필터링한다.
    // 메모를 저장한다, 수정한다.
    // 메모를 삭제한다.

    private val schedulers = Schedulers.newThread()
    private val tagViewModel = TagViewModel()

    @Test
    @DisplayName("모든 태그를 가져온다.")
    fun getAllTagTest(){

    }

}
