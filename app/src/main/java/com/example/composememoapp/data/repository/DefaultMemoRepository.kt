package com.example.composememoapp.data.repository

import com.example.composememoapp.data.database.MemoDao
import com.example.composememoapp.data.database.entity.MemoEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class DefaultMemoRepository @Inject constructor(
    private val memoDao: MemoDao,
) : MemoRepository {

    override fun getAllMemo(): Flowable<List<MemoEntity>> =
        memoDao.getAllMemo()

    override fun insertMemo(memoEntity: MemoEntity): Completable =
        memoDao.insertMemo(memoEntity = memoEntity)

    override fun deleteMemo(memoEntity: MemoEntity): Completable =
        memoDao.deleteMemo(memoEntity = memoEntity)
}
