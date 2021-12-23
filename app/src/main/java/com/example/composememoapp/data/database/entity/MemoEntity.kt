package com.example.composememoapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.composememoapp.data.MemoModel
import java.util.Date

@Entity
data class MemoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val updatedDate: Date = Date(System.currentTimeMillis()),
    val contents: List<ContentBlockEntity>,
    var isBookMarked: Boolean = false,
    var tagEntities: List<String> = listOf()
) {
    fun convertToMemoViewModel() = MemoModel(
        id = id,
        updatedDate = updatedDate,
        contents = contents.map { it.convertToContentBlockModel() },
        isBookMarked = isBookMarked,
        tagEntities = tagEntities
    )
}
