package com.example.composememoapp.domain

import android.content.Context
import com.example.composememoapp.data.MemoModel
import com.example.composememoapp.data.repository.MemoAppRepository
import javax.inject.Inject

class SaveMemoUseCase @Inject constructor(
    private val memoAppRepository: MemoAppRepository
) {
    operator fun invoke(memoModel: MemoModel, context: Context) =
        memoAppRepository.insertMemo(memoEntity = memoModel, context = context)
}
