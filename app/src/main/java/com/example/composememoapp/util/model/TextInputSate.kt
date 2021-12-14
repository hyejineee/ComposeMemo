package com.example.composememoapp.util.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class TextInputSate(
    initialText: String,
) : InputState {
    var text by mutableStateOf(initialText)

    companion object {
        val Saver: Saver<TextInputSate, *> = listSaver(
            save = { listOf(it.text) },
            restore = {
                TextInputSate(
                    initialText = it[0]
                )
            }
        )
    }
}

@Composable
fun rememberTextInputState(initialText: String): TextInputSate {
    return rememberSaveable(initialText, saver = TextInputSate.Saver) {
        TextInputSate(initialText = initialText)
    }
}
