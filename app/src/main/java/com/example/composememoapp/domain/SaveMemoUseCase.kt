package com.example.composememoapp.domain

import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.repository.MemoAppRepository
import javax.inject.Inject

class SaveMemoUseCase @Inject constructor(
    private val memoAppRepository: MemoAppRepository
) {
    operator fun invoke(memoEntity: MemoEntity) = memoAppRepository.insertMemo(memoEntity = memoEntity)
}
