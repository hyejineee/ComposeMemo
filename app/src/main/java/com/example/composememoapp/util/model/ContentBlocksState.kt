package com.example.composememoapp.util.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.composememoapp.data.ContentBlock

class ContentBlocksState(
    initialContents: List<ContentBlock<*>>,
) : InputState {

    var contents = mutableStateListOf<ContentBlock<*>>()

    init {
        initialContents.forEach {
            contents.add(it)
        }
    }

    companion object {
        val Saver: Saver<ContentBlocksState, *> = listSaver(
            save = {
                listOf(it.contents.toList())
            },
            restore = {
                ContentBlocksState(
                    initialContents = it[0]
                )
            }
        )
    }
}

@Composable
fun rememberContentBlocksState(initialContents: List<ContentBlock<*>>): ContentBlocksState {
    return rememberSaveable(initialContents, saver = ContentBlocksState.Saver) {
        ContentBlocksState(initialContents = initialContents)
    }
}
