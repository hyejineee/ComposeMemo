package com.example.composememoapp.domain

import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.repository.MemoRepository
import javax.inject.Inject

class SaveMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    operator fun invoke(memoEntity: MemoEntity) = memoRepository.insertMemo(memoEntity = memoEntity)
}
