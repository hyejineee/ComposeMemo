package com.example.composememoapp.data.database

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.database.entity.TagEntity
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Action
import kotlinx.coroutines.selects.select

@Dao
abstract class MemoDao {
    @Query("select * from MemoEntity")
    abstract fun getAllMemo(): Flowable<List<MemoEntity>>

    @Insert(onConflict = REPLACE)
    abstract fun insertMemoEntity(memoEntity: MemoEntity): Completable

    @Insert(onConflict = REPLACE)
    abstract fun insertTagEntity(tags:List<TagEntity>):Completable

    fun insertMemo(memoEntity: MemoEntity): Completable {
        val tags:List<TagEntity> = memoEntity.tagEntities.map { TagEntity(tag = it) }

        return insertMemoEntity(memoEntity = memoEntity)
            .mergeWith(insertTagEntity(tags = tags))
    }

    @Delete
    abstract fun deleteMemo(memoEntity: MemoEntity): Completable
}
