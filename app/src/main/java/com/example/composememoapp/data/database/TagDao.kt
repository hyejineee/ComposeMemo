package com.example.composememoapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.example.composememoapp.data.database.entity.TagEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
abstract class TagDao {

    @Insert(onConflict = IGNORE)
    abstract fun insertTagEntity(tags: List<TagEntity>): Completable

    @Query("select * from TagEntity")
    abstract fun getAllTag(): Flowable<List<TagEntity>>
}
