package com.example.composememoapp.data.database.entity

import com.example.composememoapp.data.ContentBlock
import com.example.composememoapp.data.ContentType
import com.example.composememoapp.data.TextBlock

data class ContentBlockEntity(
    val type: ContentType,
    var seq:Long,
    val content:String,
){
    fun convertToContentBlockModel():ContentBlock<*> = when(type){
        ContentType.Text -> TextBlock(seq = seq, content = content)
        else -> throw Exception("Not Content Type")
    }
}
