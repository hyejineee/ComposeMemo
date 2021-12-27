package com.example.composememoapp.util.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class CheckBoxState(
    initialText: String,
    initialChecked: Boolean
) : InputState {

    var text by mutableStateOf(initialText)
    var isChecked by mutableStateOf(initialChecked)

    companion object {
        private const val textKey = "text"
        private const val checkedKey = "isChecked"
        val Saver: Saver<CheckBoxState, *> = mapSaver(
            save = { mapOf(textKey to it.text, checkedKey to it.isChecked) },
            restore = {
                CheckBoxState(
                    initialText = it[textKey] as String,
                    initialChecked = it[checkedKey] as Boolean
                )
            }
        )
    }
}

@Composable
fun rememberCheckBoxState(initialText: String, initialChecked: Boolean): CheckBoxState {
    return rememberSaveable(saver = CheckBoxState.Saver) {
        CheckBoxState(initialText = initialText, initialChecked = initialChecked)
    }
}
