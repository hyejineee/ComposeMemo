package com.example.composememoapp.data.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(indices = arrayOf(Index(value = ["tag"], unique = true)))
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val tag: String
) : Parcelable
