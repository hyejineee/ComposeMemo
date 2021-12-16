package com.example.composememoapp.presentation.ui.detailandwrite

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.data.ContentBlock
import com.example.composememoapp.data.ContentType
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.util.model.ContentBlocksState
import com.example.composememoapp.util.model.rememberContentBlocksState
import com.example.composememoapp.util.model.rememberTextInputState

@ExperimentalComposeUiApi
@Composable
fun DetailAndWriteScreen(
    memoEntity: MemoEntity? = null,
    memoViewModel: MemoViewModel,
    handleBackButtonClick: () -> Unit,
) {

    var contentsState = rememberContentBlocksState(
        initialContents = memoEntity
            ?.contents
            ?.map { it.convertToContentBlockModel() }
            ?.toMutableList()
            ?: mutableListOf(TextBlock(1, ""))
    )

    val handleSaveMemo = {
        val newMemo = saveMemo(memoEntity = memoEntity, contentsState = contentsState)
        memoViewModel.saveMemo(newMemo)
    }

    val handleAddDefaultBlock: (FocusRequester, SoftwareKeyboardController?) -> Unit =
        { f: FocusRequester, k: SoftwareKeyboardController? ->
            contentsState.contents.add(TextBlock(contentsState.contents.last().seq + 1, ""))
            f.requestFocus()
            k?.show()
        }

    BackHandler() {
        handleBackButtonClick()
    }

    DetailAndWriteScreenContent(
        memoEntity = memoEntity,
        contents = contentsState.contents,
        handleBackButtonClick = handleBackButtonClick,
        handleSaveMemo = handleSaveMemo,
        handleAddDefaultBlock = handleAddDefaultBlock,
    )
}

private fun saveMemo(memoEntity: MemoEntity?, contentsState: ContentBlocksState): MemoEntity {
    val newContents = contentsState
        .contents
        .map { block ->
            block.convertToContentBlockEntity()
        }
        .filter { block ->
            block.content.isNotBlank()
        }
        .mapIndexed { index, contentBlockEntity ->
            contentBlockEntity.seq = index + 1L
            contentBlockEntity
        }

    val newMemo = memoEntity?.let {
        it.copy(
            contents = newContents
        )
    } ?: MemoEntity(
        contents = newContents
    )

    return newMemo
}

@ExperimentalComposeUiApi
@Composable
fun DetailAndWriteScreenContent(
    memoEntity: MemoEntity?,
    contents: List<ContentBlock<*>>,
    handleAddDefaultBlock: (FocusRequester, SoftwareKeyboardController?) -> Unit,
    handleBackButtonClick: () -> Unit,
    handleSaveMemo: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            TopAppBar() {
                Icon(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                handleSaveMemo()
                                handleBackButtonClick()
                            }
                        )
                        .padding(10.dp),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier
            .clickable { handleAddDefaultBlock(focusRequester, keyboardController) }
            .fillMaxSize()
    ) {

        ContentBlocks(
            contents = contents,
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
                ContentBlockEntity(
                    type = ContentType.Text,
                    seq = it.toLong(),
                    content = "this is text block content $it" +
                        " this is text block content $it" +
                        " this is text block content $it"
                )
            }
        )
        DetailAndWriteScreenContent(
            memoEntity = memo,
            handleBackButtonClick = {},
            handleAddDefaultBlock = { f, k -> },
            handleSaveMemo = {},
            contents = memo.contents.map { it.convertToContentBlockModel() }
        )
    }
}
