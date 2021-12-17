package com.example.composememoapp.domain

import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.repository.MemoRepository

class DeleteMemoUseCase(
    private val memoRepository: MemoRepository
) {
    operator fun invoke(memoEntity: MemoEntity) = memoRepository.deleteMemo(memoEntity)
}
