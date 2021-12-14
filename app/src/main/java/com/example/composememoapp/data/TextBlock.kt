package com.example.composememoapp.data

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.example.composememoapp.presentation.ui.component.TextInput
import com.example.composememoapp.util.model.InputState
import com.example.composememoapp.util.model.TextInputSate

@kotlinx.parcelize.Parcelize
data class TextBlock(
    override var seq: Int,
    override var content: String
) : ContentBlock<String>, Parcelable {

    @Composable
    override fun drawOnlyReadContent(modifier: androidx.compose.ui.Modifier) {
        Text(text = content, fontSize = 13.sp)
    }

    @Composable
    override fun drawEditableContent(state: InputState, modifier: androidx.compose.ui.Modifier) {
        val textInputState = (state as TextInputSate)

        Box(modifier = modifier) {
            TextInput(
                text = textInputState.text,
                onValueChange = {
                    textInputState.text = it
                }
            )
        }
    }
}
