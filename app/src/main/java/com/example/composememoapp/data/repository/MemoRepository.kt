package com.example.composememoapp.data.repository

import com.example.composememoapp.data.entity.MemoEntity
import kotlinx.coroutines.flow.Flow

interface MemoRepository {
    fun getAllMemo(): Flow<List<MemoEntity>>
    fun insertMemo(memoEntity: MemoEntity)
}
