package com.example.composememoapp.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = arrayOf(Index(value = ["tag"], unique = true)))
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id:Long? = null,
    val tag:String? = null
)
