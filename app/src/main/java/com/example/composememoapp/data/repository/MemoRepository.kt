package com.example.composememoapp.data.repository

import com.example.composememoapp.data.database.entity.MemoEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface MemoRepository {
    fun getAllMemo(): Flowable<List<MemoEntity>>
    fun insertMemo(memoEntity: MemoEntity): Completable
}
