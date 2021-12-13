package com.example.composememoapp.data

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.composememoapp.presentation.ui.component.TextInput
import com.example.composememoapp.util.model.InputState
import com.example.composememoapp.util.model.TextInputSate

data class TextBlock(
    override var seq: Int,
    override var content: String
) : ContentBlock<String> {

    @Composable
    override fun drawOnlyReadContent() {
        Text(text = content)
    }

    @Composable
    override fun drawEditableContent(state: InputState) {
        val textInputState = (state as TextInputSate)

        TextInput(
            text = textInputState.text,
            onValueChange = {
                textInputState.text = it
            }
        )
    }
}
