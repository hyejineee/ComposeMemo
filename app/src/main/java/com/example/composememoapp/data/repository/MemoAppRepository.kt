package com.example.composememoapp.data.repository

import android.content.Context
import com.example.composememoapp.data.MemoModel
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.database.entity.TagEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface MemoAppRepository {
    fun getAllMemo(): Flowable<List<MemoEntity>>
    fun insertMemo(memoEntity: MemoModel, context: Context): Completable
    fun deleteMemo(memoEntity: MemoEntity): Completable
    fun getAllTag(): Flowable<List<TagEntity>>
}
