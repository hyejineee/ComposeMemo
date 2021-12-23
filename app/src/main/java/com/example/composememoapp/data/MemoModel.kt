package com.example.composememoapp.data

import com.example.composememoapp.data.database.entity.MemoEntity
import java.util.Date

data class MemoModel(
    val id: Long? = null,
    val updatedDate: Date = Date(System.currentTimeMillis()),
    var contents: List<ContentBlock<*>>,
    var isBookMarked: Boolean = false,
    var tagEntities: List<String> = listOf()
) {
    fun convertToMemoEntity() = MemoEntity(
        id = id,
        updatedDate = updatedDate,
        contents = contents.map { it.convertToContentBlockEntity() },
        isBookMarked = isBookMarked,
        tagEntities = tagEntities
    )
}
