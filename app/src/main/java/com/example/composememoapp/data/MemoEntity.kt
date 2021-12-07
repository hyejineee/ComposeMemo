package com.example.composememoapp.data

import java.util.Date

data class MemoEntity(
    val id: Int,
    val updatedDate: Date = Date(System.currentTimeMillis()),
    val contents: List<ContentBlock<*>>,
    val isBookMarked: Boolean = false,
    val tagEntities: List<TagEntity> = emptyList()
)
