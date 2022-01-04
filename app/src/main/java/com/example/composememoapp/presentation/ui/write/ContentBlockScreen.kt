package com.example.composememoapp.presentation.ui.write

import android.net.Uri
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.CheckBoxBlock
import com.example.composememoapp.presentation.ui.component.blocks.ContentBlock
import com.example.composememoapp.presentation.ui.component.blocks.ImageBlock
import com.example.composememoapp.presentation.ui.component.blocks.TextBlock
import com.example.composememoapp.presentation.viewModel.ContentBlockViewModel

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun ContentBlockScreen(
    contentBlockViewModel: ContentBlockViewModel,
    contents: List<ContentBlock<*>>,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var index by rememberSaveable { mutableStateOf<Int?>(null) }

    val handleCursorPosition = { i: Int ->
        index = if (i == 0) {
            1
        } else {
            i
        }
    }

    val handleAddDefaultBlock: (Int?) -> Unit = { index: Int? ->
        contentBlockViewModel.insertTextBlock(index = index)
    }

    val handleAddImageBlock = { i: Int? ->
        { uri: Uri? ->
            uri?.let {
                contentBlockViewModel.insertImageBlock(i, it)
            }
            Unit
        }
    }

    val handleAddCheckBoxBlock = { i: Int? ->
        contentBlockViewModel.insertCheckBoxBlock(i)
    }

    val handleDeleteBlock = { block: ContentBlock<*> ->
        contentBlockViewModel.deleteBlock(block)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        ContentBlocks(
            contents = contents,
            handleCursorPosition = handleCursorPosition,
            handleAddDefaultBlock = handleAddDefaultBlock,
            handleDeleteBlock = handleDeleteBlock,
            focusRequester = focusRequester,
            keyboardController = keyboardController,
            focusedIndex = index
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            WriteScreenBottomBar(
                handleAddImage = handleAddImageBlock(index),
                handleAddCheckBox = { handleAddCheckBoxBlock(index) }
            )
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun ContentBlocks(
    contents: List<ContentBlock<*>>,
    focusedIndex: Int? = null,
    handleCursorPosition: (Int) -> Unit,
    handleAddDefaultBlock: (Int?) -> Unit,
    handleDeleteBlock: (ContentBlock<*>) -> Unit,
    focusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?
) {

    val scrollState = rememberScrollState()

    Log.d("ContentBlocks", "focusedIndex = $focusedIndex")

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 50.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        for (i in contents.indices) {

            val index = i + 1

            val focusRequesterModifier = if (focusedIndex ?: 0 + 1 == index) {
                Modifier.focusRequester(focusRequester = focusRequester)
            } else {
                Modifier
            }.then(
                Modifier.onFocusEvent {
                    if (it.isFocused) {
                        handleCursorPosition(index)
                    }
                }
            )

            SideEffect {
                focusRequester.requestFocus()
                keyboardController?.show()
            }

            when (val content = contents[i]) {
                is TextBlock -> {
                    content.drawEditableContent(
                        modifier = focusRequesterModifier
                            .padding(2.dp)
                            .onPreviewKeyEvent {
                                if (it.key.keyCode == Key.Backspace.keyCode) {
                                    if (content.content.isBlank()) {
                                        handleCursorPosition(index - 1)
                                        handleDeleteBlock(content)
                                    }
                                }
                                false
                            },
                        handleAddDefaultBlock = {
                            handleCursorPosition(index + 1)
                            handleAddDefaultBlock(index)
                        }
                    )
                }
                is ImageBlock -> {
                    content.drawEditableContent(
                        modifier = focusRequesterModifier
                            .padding(2.dp)
                            .focusable(true)
                            .onKeyEvent {
                                if (it.key.keyCode == Key.Backspace.keyCode) {
                                    if(focusedIndex == index){
                                        handleCursorPosition(index - 1)
                                        handleDeleteBlock(content)
                                    }
                                }
                                false
                            }
                            .clickable {
                                handleCursorPosition(index)
                            }
                    )
                }

                is CheckBoxBlock -> {
                    content.drawEditableContent(
                        modifier = focusRequesterModifier
                            .onKeyEvent {
                                if (it.key.keyCode == Key.Backspace.keyCode) {
                                    if (content.content.text.isBlank()) {
                                        handleCursorPosition(index - 1)
                                        handleDeleteBlock(content)
                                    }
                                }
                                false
                            }
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Preview
@Composable
fun ContentBlocksPreviwe() {
    ComposeMemoAppTheme() {
        ContentBlocks(
            contents = emptyList(),
            handleCursorPosition = {},
            handleAddDefaultBlock = {},
            handleDeleteBlock = {},
            focusRequester = FocusRequester(),
            keyboardController = LocalSoftwareKeyboardController.current
        )
    }
}
