package com.example.composememoapp.presentation.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.composememoapp.data.CheckBoxBlock
import com.example.composememoapp.data.ContentBlock
import com.example.composememoapp.data.ImageBlock
import com.example.composememoapp.data.TextBlock

@ExperimentalComposeUiApi
@Composable
fun ContentBlocks(
    contents: List<ContentBlock<*>>,
    focusRequester: FocusRequester,
    handleCursorPosition: (Int) -> Unit,
    keyboardController: SoftwareKeyboardController?
) {

    Column(modifier = Modifier.padding(16.dp)) {
        for (i in contents.indices) {
            when (val content = contents[i]) {
                is TextBlock -> {
                    LaunchedEffect(key1 = content) {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }

                    content.textInputState = remember {
                        mutableStateOf(
                            TextFieldValue(
                                text = content.content,
                                selection = TextRange(content.content.length)
                            )
                        )
                    }

                    content.drawEditableContent(
                        modifier = Modifier
                            .focusRequester(focusRequester = focusRequester)
                            .onFocusChanged {
                                if (it.isFocused) {
                                    handleCursorPosition(i + 1)
                                }
                            }
                            .padding(2.dp)
                            .semantics {
                                this.contentDescription = "text block ${content.seq}"
                            }
                    )
                }
                is ImageBlock -> {
                    content.imageState = remember {
                        mutableStateOf<Bitmap?>(null)
                    }

                    content.drawEditableContent(modifier = Modifier.padding(2.dp))
                }

                is CheckBoxBlock -> {

                    content.checkState = remember { mutableStateOf(content.content.isChecked) }
                    content.textInputState = mutableStateOf(
                        TextFieldValue(
                            text = content.content.text,
                            selection = TextRange(content.content.text.length)
                        )
                    )

                    content.drawEditableContent(
                        modifier = Modifier
                            .focusRequester(focusRequester = focusRequester)
                            .onFocusChanged {
                                if (it.isFocused) {
                                    handleCursorPosition(i + 1)
                                }
                            }
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}
