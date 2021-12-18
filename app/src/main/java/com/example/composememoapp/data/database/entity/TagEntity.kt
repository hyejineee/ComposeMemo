package com.example.composememoapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id:Long? = null,
    val tag:String? = null
)
