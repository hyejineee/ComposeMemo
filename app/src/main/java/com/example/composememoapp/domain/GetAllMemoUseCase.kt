package com.example.composememoapp.domain

import com.example.composememoapp.data.repository.MemoRepository
import javax.inject.Inject

class GetAllMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    operator fun invoke() = memoRepository.getAllMemo()
}
