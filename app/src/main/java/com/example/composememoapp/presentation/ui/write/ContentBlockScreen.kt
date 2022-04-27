package com.example.composememoapp.presentation.ui.write

import android.net.Uri
import android.util.Log
import android.view.KeyEvent
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
import androidx.compose.ui.modifier.modifierLocalConsumer
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
            contentBlockViewModel = contentBlockViewModel,
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
    contentBlockViewModel: ContentBlockViewModel
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 50.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        val focusManager = LocalFocusManager.current

        for (i in contents.indices) {

            val content = contents[i]
            val modifier = Modifier
                .onFocusChanged {
                    if (it.isFocused) {
                        handleFocusedIndex(i)
                    }
                }
                .onPreviewKeyEvent {
                    if (it.key.nativeKeyCode == Key.Backspace.nativeKeyCode) {
                        Log.d("ContentBlock", "click backspace")

                        if (content.isEmpty()) {
                            handleDeleteBlock(content)
                            focusManager.moveFocus(FocusDirection.Up)
                        }
                    }
                    true
                }

            content.drawEditableContent(modifier = modifier, viewModel = contentBlockViewModel)
        }
    }
}
