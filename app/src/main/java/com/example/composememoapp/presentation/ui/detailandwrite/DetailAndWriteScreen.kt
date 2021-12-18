package com.example.composememoapp.presentation.ui.detailandwrite

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.example.composememoapp.presentation.ui.component.ContentBlocks
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.util.model.rememberContentBlocksState

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
        val newMemoEntity = memoViewModel.makeMemoEntity(
            memoEntity = memoEntity,
            contents = contentsState.contents,
            tags = tagState.tags
        )

        if (newMemoEntity.contents.isNotEmpty()) {
            memoViewModel.saveMemo(memoEntity = memoEntity)
        }
    }

    val handleDeleteMemo = { memo: MemoEntity ->
        memoViewModel.deleteMemo(memo)
    }

    val handleAddDefaultBlock: (FocusRequester, SoftwareKeyboardController?) -> Unit =
        { f: FocusRequester, k: SoftwareKeyboardController? ->
            contentsState.contents.add(TextBlock(contentsState.contents.last().seq + 1, ""))
            f.requestFocus()
            k?.show()
        }

    BackHandler() {
        handleSaveMemo()
        handleBackButtonClick()
    }

    DetailAndWriteScreenContent(
        memoEntity = memoEntity,
        contents = contentsState.contents,
        handleDeleteMemo = handleDeleteMemo,
        handleBackButtonClick = handleBackButtonClick,
        handleSaveMemo = handleSaveMemo,
        handleAddDefaultBlock = handleAddDefaultBlock,
    )
}

@ExperimentalComposeUiApi
@Composable
fun DetailAndWriteScreenContent(
    memoEntity: MemoEntity?,
    contents: List<ContentBlock<*>>,
    handleDeleteMemo: (MemoEntity) -> Unit,
    handleAddDefaultBlock: (FocusRequester, SoftwareKeyboardController?) -> Unit,
    handleBackButtonClick: () -> Unit,
    handleSaveMemo: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        topBar = {
            TopAppBar() {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
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

                    Icon(
                        imageVector = Icons.TwoTone.Delete,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                memoEntity?.let {
                                    handleDeleteMemo(it)
                                }
                                handleBackButtonClick()
                            }
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                    )
                }
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
            handleDeleteMemo = {},
            contents = memo.contents.map { it.convertToContentBlockModel() }
        )
    }
}
