package com.example.composememoapp.presentation.ui.component

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.example.composememoapp.data.database.entity.ContentBlockEntity

@kotlinx.parcelize.Parcelize
data class TextBlock(
    override var seq: Long,
    override var content: String,
) : ContentBlock<String>, Parcelable {

    lateinit var textInputState: MutableState<TextFieldValue>

    @Composable
    override fun drawOnlyReadContent(modifier: androidx.compose.ui.Modifier) {
        Box(modifier = modifier) {
            Text(text = content, fontSize = 13.sp)
        }
    }

    @Composable
    override fun drawEditableContent(modifier: androidx.compose.ui.Modifier) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TextInput(
                value = textInputState.value,
                onValueChange = {
                    textInputState.value = it
                    content = it.text
                },
                modifier = modifier
                    .fillMaxWidth()
            )
        }
    }

    override fun convertToContentBlockEntity() = ContentBlockEntity(
        type = ContentType.Text,
        seq = seq,
        content = content
    )
}
