package com.example.composememoapp.presentation.ui.write

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.composememoapp.presentation.ui.component.CheckBoxBlock
import com.example.composememoapp.presentation.ui.component.blocks.ContentBlock
import com.example.composememoapp.presentation.ui.component.blocks.ImageBlock
import com.example.composememoapp.presentation.ui.component.blocks.TextBlock

@ExperimentalComposeUiApi
@Composable
fun ContentBlocks(
    contents: List<ContentBlock<*>>,
    focusedIndex: Int? = null,
    handleCursorPosition: (Int) -> Unit,
    handleAddDefaultBlock: (Int?)-> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        for (i in contents.indices) {

            val index = i + 1

            val focusRequesterModifier = if (focusedIndex ?: 0 + 1 == index) {
                Modifier.focusRequester(focusRequester = focusRequester)
            } else {
                Modifier
            }.then(Modifier.onFocusEvent {
                if (it.isFocused) {
                    handleCursorPosition(index)
                }
            })

            SideEffect {
                focusRequester.requestFocus()
                keyboardController?.show()
            }

            when (val content = contents[i]) {
                is TextBlock -> {

                    content.drawEditableContent(
                        modifier = focusRequesterModifier
                            .padding(2.dp)
                            .semantics {
                                this.contentDescription = "text block ${content.seq}"
                            },
                        handleAddDefaultBlock = {
                            handleCursorPosition(index + 1)
                            handleAddDefaultBlock(index)
                        }

                    )
                }
                is ImageBlock -> {

                    content.drawEditableContent(modifier = focusRequesterModifier
                        .padding(2.dp)
                        .focusable(true)
                        .clickable {
                            handleCursorPosition(index)
                        }
                    )
                }

                is CheckBoxBlock -> {

                    content.drawEditableContent(
                        modifier = focusRequesterModifier
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}
