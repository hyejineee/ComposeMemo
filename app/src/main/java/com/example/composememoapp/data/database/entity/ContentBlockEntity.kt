package com.example.composememoapp.data.database.entity

import android.net.Uri
import com.example.composememoapp.data.CheckBoxBlock
import com.example.composememoapp.data.CheckBoxModel
import com.example.composememoapp.data.ContentBlock
import com.example.composememoapp.data.ContentType
import com.example.composememoapp.data.ImageBlock
import com.example.composememoapp.data.TextBlock
import com.google.gson.Gson

data class ContentBlockEntity(
    val type: ContentType,
    var seq: Long,
    val content: String,
) {
    fun convertToContentBlockModel(): ContentBlock<*> = when (type) {
        ContentType.Text -> TextBlock(seq = seq, content = content)
        ContentType.Image -> ImageBlock(seq = seq, content = Uri.parse(content))
        ContentType.CheckBox -> CheckBoxBlock(seq = seq, content = Gson().fromJson(content, CheckBoxModel::class.java))
        else -> throw Exception("Not Content Type")
    }
}
