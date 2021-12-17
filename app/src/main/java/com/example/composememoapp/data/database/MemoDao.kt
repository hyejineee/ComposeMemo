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
interface MemoDao {
    @Query("select * from MemoEntity")
    fun getAllMemo(): Flowable<List<MemoEntity>>

    @Insert(onConflict = REPLACE)
    fun insertMemo(memoEntity: MemoEntity): Completable

    @Delete
    fun deleteMemo(memoEntity: MemoEntity): Completable
}
