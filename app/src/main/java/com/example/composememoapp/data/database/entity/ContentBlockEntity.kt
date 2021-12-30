package com.example.composememoapp.data.database.entity

import android.net.Uri
import com.example.composememoapp.presentation.ui.component.CheckBoxBlock
import com.example.composememoapp.presentation.ui.component.CheckBoxModel
import com.example.composememoapp.presentation.ui.component.blocks.ContentBlock
import com.example.composememoapp.presentation.ui.component.blocks.ContentType
import com.example.composememoapp.presentation.ui.component.blocks.ImageBlock
import com.example.composememoapp.presentation.ui.component.blocks.TextBlock
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
