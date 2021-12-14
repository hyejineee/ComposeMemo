package com.example.composememoapp.viewModel

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MemoViewModelTest {

    // 메모 리스트를 디비에서 가져온다.
    // 메모를 카테고리에 따라 필터링한다.
    // 메모를 저장, 수정, 삭제한다.

    @Test
    @DisplayName("junit 5 테스트")
    fun junit(){
        val sum = 1+5
        assertThat(sum).isEqualTo(6)
    }
}
