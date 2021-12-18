package com.example.composememoapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class MemoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val updatedDate: Date = Date(System.currentTimeMillis()),
    val contents: List<ContentBlockEntity>,
    val isBookMarked: Boolean = false,
    val tagEntities: String = ""
)
