package com.example.composememoapp.presentation.ui.write

import android.net.Uri
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
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

    val handleAddImageBlock = { uri: Uri? ->
        uri?.let {
            contentBlockViewModel.changeToImageBlock(it)
        }
        Unit
    }

    val handleAddCheckBoxBlock = {
        contentBlockViewModel.changeToCheckBoxBlock()
    }

    val handleAddTextBlock = {
        contentBlockViewModel.insertTextBlock()
    }

    val handleDeleteBlock = { block: ContentBlock<*> ->
        contentBlockViewModel.deleteBlock(block)
    }

    val handleFocusedIndex = { index: Int ->
        contentBlockViewModel.focusedBlock(index = index)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        ContentBlocks(
            contents = contents,
            handleDeleteBlock = handleDeleteBlock,
            handleFocusedIndex = handleFocusedIndex,
            handleAddTextBlock = handleAddTextBlock
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            WriteScreenBottomBar(
                handleAddImage = handleAddImageBlock,
                handleAddCheckBox = handleAddCheckBoxBlock
            )
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun ContentBlocks(
    contents: List<ContentBlock<*>>,
    handleDeleteBlock: (ContentBlock<*>) -> Unit,
    handleFocusedIndex: (Int) -> Unit,
    handleAddTextBlock: () -> Unit,
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 50.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        val focusManager = LocalFocusManager.current
        val addTextBlock: () -> Unit = {
            handleAddTextBlock()
            focusManager.moveFocus(FocusDirection.Down)
        }

        for (i in contents.indices) {

            val focusedModifier = Modifier.onFocusChanged {
                if (it.isFocused) {
                    handleFocusedIndex(i)
                }
            }

            when (val content = contents[i]) {
                is TextBlock -> {
                    content.drawEditableContent(
                        modifier = focusedModifier
                            .padding(2.dp)
                            .onPreviewKeyEvent {
                                if (it.key.nativeKeyCode == Key.Backspace.nativeKeyCode) {
                                    if (content.content.isBlank()) {
                                        handleDeleteBlock(content)
                                        focusManager.moveFocus(FocusDirection.Up)
                                    }
                                }
                                false
                            },
                        addTextBlock
                    )
                }
                is ImageBlock -> {
                    content.drawEditableContent(
                        modifier = focusedModifier
                            .padding(2.dp)
                            .focusable(true)
                            .onKeyEvent {
                                if (it.key.nativeKeyCode == Key.Backspace.nativeKeyCode) {
                                    handleDeleteBlock(content)
                                    focusManager.moveFocus(FocusDirection.Up)
                                }
                                false
                            }

                    )
                }

                is CheckBoxBlock -> {
                    content.drawEditableContent(
                        modifier = focusedModifier
                            .padding(2.dp)
                            .onPreviewKeyEvent {
                                if (it.key.nativeKeyCode == Key.Backspace.nativeKeyCode) {
                                    if (content.textInputState.value.text.isBlank()) {
                                        handleDeleteBlock(content)
                                        focusManager.moveFocus(FocusDirection.Up)
                                    }
                                }
                                false
                            },
                        addTextBlock
                    )
                }
            }
        }
    }
}
