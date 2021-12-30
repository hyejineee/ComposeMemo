package com.example.composememoapp.data.repository

import com.example.composememoapp.data.database.MemoDao
import com.example.composememoapp.data.database.TagDao
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.database.entity.TagEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class DefaultMemoAppRepository @Inject constructor(
    private val memoDao: MemoDao,
    private val tagDao: TagDao
) : MemoAppRepository {

    override fun getAllMemo(): Flowable<List<MemoEntity>> =
        memoDao.getAllMemo()

    override fun insertMemo(memoEntity: MemoEntity): Completable {
        return memoDao.insertMemoEntity(memoEntity)
            .mergeWith(tagDao.insertTagEntity(memoEntity.tagEntities.map { tag -> TagEntity(tag = tag) }))
    }

    override fun deleteMemo(memoEntity: MemoEntity): Completable =
        memoDao.deleteMemo(memoEntity = memoEntity)

    override fun getAllTag(): Flowable<List<TagEntity>> = tagDao.getAllTag()
}
