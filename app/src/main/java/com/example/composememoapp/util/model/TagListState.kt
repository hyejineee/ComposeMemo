package com.example.composememoapp.util.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class TagListState(
    initialContents: List<String>,
) {
    var tags by mutableStateOf(initialContents)

    companion object {
        val Saver: Saver<TagListState, *> = listSaver(
            save = { listOf(it.tags) },
            restore = {
                TagListState(
                    initialContents = it[0]
                )
            }
        )
    }
}

@Composable
fun rememberTagListState(initialContents: List<String>): TagListState {
    return rememberSaveable(initialContents, saver = TagListState.Saver) {
        TagListState(initialContents = initialContents)
    }
}
