package com.example.composememoapp.data.entity

import com.example.composememoapp.data.ContentBlock
import java.util.Date

data class MemoEntity(
    val id: Int,
    val updatedDate: Date = Date(System.currentTimeMillis()),
    val contents: List<ContentBlock<*>>,
    val isBookMarked: Boolean = false,
    val tagEntities: List<TagEntity> = emptyList()
)
