package com.example.composememoapp.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.composememoapp.data.ContentBlock
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.util.model.rememberTextInputState

@ExperimentalComposeUiApi
@Composable
fun ContentBlocks(contents: List<ContentBlock<*>>, focusRequester: FocusRequester) {

    Column(modifier = Modifier.padding(16.dp)) {
        for (content in contents) {
            when (content) {
                is TextBlock -> {
                    content.textInputState = rememberTextInputState(initialText = content.content)
                    content.drawEditableContent(
                        state = content.textInputState,
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .padding(5.dp)
                            .semantics {
                                this.contentDescription = "text block ${content.seq}"
                            }
                    )
                }
            }
        }
    }
}
