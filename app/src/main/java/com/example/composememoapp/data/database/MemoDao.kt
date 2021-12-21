package com.example.composememoapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.composememoapp.data.database.entity.MemoEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
abstract class MemoDao {
    @Query("select * from MemoEntity")
    abstract fun getAllMemo(): Flowable<List<MemoEntity>>

    @Insert(onConflict = REPLACE)
    abstract fun insertMemoEntity(memoEntity: MemoEntity): Completable

    @Delete
    abstract fun deleteMemo(memoEntity: MemoEntity): Completable
}
