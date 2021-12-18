package com.example.composememoapp.domain

import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.repository.MemoAppRepository

class DeleteMemoUseCase(
    private val memoAppRepository: MemoAppRepository
) {
    operator fun invoke(memoEntity: MemoEntity) = memoAppRepository.deleteMemo(memoEntity)
}
