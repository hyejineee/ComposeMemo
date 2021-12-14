package com.example.composememoapp.presentation.ui.detailandwrite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.data.ContentBlock
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.data.entity.MemoEntity
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.util.model.rememberContentBlocksState
import com.example.composememoapp.util.model.rememberTextInputState

@ExperimentalComposeUiApi
@Composable
fun DetailAndWriteScreen(
    memoEntity: MemoEntity? = null,
    viewModel: MemoViewModel = MemoViewModel(),
    handleBackButtonClick: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    var contentsState = rememberContentBlocksState(
        initialContents = memoEntity
            ?.contents
            ?.toMutableList()
            ?: mutableListOf(TextBlock(1, ""))
    )

    Scaffold(
        topBar = {
            TopAppBar() {
                Icon(
                    modifier = Modifier
                        .clickable(onClick = handleBackButtonClick)
                        .padding(10.dp),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier
            .clickable {
                contentsState.contents.add(TextBlock(contentsState.contents.last().seq + 1, ""))
                focusRequester.requestFocus()
                keyboardController?.show()
            }
            .fillMaxSize()
    ) {

        ContentBlocks(
            contents = contentsState.contents,
            focusRequester = focusRequester
        )
    }
}

@Composable
fun ContentBlocks(contents: List<ContentBlock<*>>, focusRequester: FocusRequester) {
    Column(modifier = Modifier.padding(16.dp)) {
        for (content in contents) {
            when (content) {
                is TextBlock -> {
                    val textInputState = rememberTextInputState(initialText = content.content)
                    content.drawEditableContent(
                        state = textInputState,
                        modifier = Modifier
                            .focusRequester(focusRequester)
                    )
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DetailAndWriteScreenPreview() {
    ComposeMemoAppTheme() {
        val memo = MemoEntity(
            id = 0,
            contents = List(10) {
                TextBlock(
                    seq = it,
                    content = "this is text block content $it" +
                        " this is text block content $it" +
                        " this is text block content $it"
                )
            }
        )
        DetailAndWriteScreen(memoEntity = memo, handleBackButtonClick = {})
    }
}
