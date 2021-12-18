package com.example.composememoapp.util.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.composememoapp.data.ContentBlock

class ContentBlocksState(
    initialContents: MutableList<ContentBlock<*>>,
) : InputState {

    var contents by mutableStateOf(initialContents)

    companion object {
        val Saver: Saver<ContentBlocksState, *> = listSaver(
            save = { listOf(it.contents) },
            restore = {
                ContentBlocksState(
                    initialContents = it[0]
                )
            }
        )
    }
}

@Composable
fun rememberContentBlocksState(initialContents: MutableList<ContentBlock<*>>): ContentBlocksState {
    return rememberSaveable(initialContents, saver = ContentBlocksState.Saver) {
        ContentBlocksState(initialContents = initialContents)
    }
}
