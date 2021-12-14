package com.example.composememoapp.data.repository

import com.example.composememoapp.data.entity.MemoEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.flow.Flow

interface MemoRepository {
    fun getAllMemo(): Flowable<List<MemoEntity>>
    fun insertMemo(memoEntity: MemoEntity):Completable
}
