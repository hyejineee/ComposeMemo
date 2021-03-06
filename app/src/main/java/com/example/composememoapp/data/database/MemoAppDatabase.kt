package com.example.composememoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.database.entity.TagEntity

@Database(
    entities = [
        MemoEntity::class,
        TagEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(com.example.composememoapp.data.database.TypeConverters::class)
abstract class MemoAppDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
    abstract fun tagDao(): TagDao
}
