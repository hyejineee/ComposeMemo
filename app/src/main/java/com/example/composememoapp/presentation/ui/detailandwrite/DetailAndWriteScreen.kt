package com.example.composememoapp.presentation.ui.detailandwrite

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
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
import com.example.composememoapp.presentation.ui.component.WriteScreenTopAppBar
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.presentation.viewModel.TagViewModel
import com.example.composememoapp.util.model.rememberContentBlocksState
import com.example.composememoapp.util.model.rememberTagListState

@ExperimentalComposeUiApi
@Composable
fun DetailAndWriteScreen(
    memoEntity: MemoEntity? = null,
    tagViewModel: TagViewModel,
    memoViewModel: MemoViewModel,
    handleBackButtonClick: () -> Unit,
) {

    val allTag by tagViewModel.tagList.subscribeAsState(initial = emptyList())

    val contentsState = rememberContentBlocksState(
        initialContents = memoEntity
            ?.contents
            ?.map { it.convertToContentBlockModel() }
            ?: listOf(TextBlock(1, ""))
    )

    val tagState = rememberTagListState(
        initialContents = memoEntity?.tagEntities ?: listOf()
    )

    val handleSaveMemo = {
        val newMemoEntity = memoViewModel.makeMemoEntity(
            memoEntity = memoEntity,
            contents = contentsState.contents,
            tags = tagState.tags
        )

        if (newMemoEntity.contents.isNotEmpty()) {
            memoViewModel.saveMemo(memoEntity = newMemoEntity)
        }
    }

    val handleDeleteMemo = { memo: MemoEntity ->
        memoViewModel.deleteMemo(memo)
    }

    val handleAddDefaultBlock: () -> Unit =
        {
            contentsState.contents =
                contentsState.contents.plus(
                    TextBlock(contentsState.contents.last().seq + 1, "")
                )
        }

    val handleClickAddTag: (String) -> Unit = { s: String ->
        if (s !in tagState.tags) {
            tagState.tags = tagState.tags.plus(s)
        }
    }



    BackHandler() {
        handleSaveMemo()
        handleBackButtonClick()
    }

    DetailAndWriteScreenContent(
        memoEntity = memoEntity,
        allTag = allTag.map { it.tag },
        contents = contentsState.contents,
        tagList = tagState.tags,
        handleDeleteMemo = handleDeleteMemo,
        handleBackButtonClick = handleBackButtonClick,
        handleSaveMemo = handleSaveMemo,
        handleAddDefaultBlock = handleAddDefaultBlock,
        handleClickAddTag = handleClickAddTag
    )
}

@ExperimentalComposeUiApi
@Composable
fun DetailAndWriteScreenContent(
    memoEntity: MemoEntity?,
    tagList: List<String>,
    allTag: List<String>,
    contents: List<ContentBlock<*>>,
    handleDeleteMemo: (MemoEntity) -> Unit,
    handleAddDefaultBlock: () -> Unit,
    handleBackButtonClick: () -> Unit,
    handleSaveMemo: () -> Unit,
    handleClickAddTag: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val scrollState = rememberScrollState()
    var favoriteSate = rememberSaveable {
        mutableStateOf(memoEntity?.isBookMarked ?: false)
    }

    val handleClickBackButton = {
        handleSaveMemo()
        handleBackButtonClick()
    }

    val handleClickFavoriteButton = {
        favoriteSate.value = !favoriteSate.value
        memoEntity?.isBookMarked = favoriteSate.value
    }

    val handleClickDeleteButton = {
        memoEntity?.let {
            handleDeleteMemo(it)
        }
        handleBackButtonClick()
    }

    DisposableEffect(Unit) {
        onDispose {
            keyboardController?.hide()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar() {
                WriteScreenTopAppBar(
                    handleClickBackButton = handleClickBackButton,
                    handleClickFavoriteButton = handleClickFavoriteButton,
                    handleClickDeleteButton = handleClickDeleteButton,
                    isFavorite = favoriteSate.value,
                    showMenuIcon = memoEntity != null
                )
            }
        },
        modifier = Modifier
            .clickable {
                handleAddDefaultBlock()
            }
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxHeight()
        ) {

            TagScreen(
                tagList = tagList,
                allTag = allTag,
                handleClickAddTag = handleClickAddTag,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()
            )

            ContentBlocks(
                contents = contents,
                focusRequester = focusRequester,
                keyboardController = keyboardController
            )
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
            allTag = listOf(),
            tagList = listOf(),
            handleClickAddTag = {},
            handleBackButtonClick = {},
            handleAddDefaultBlock = { },
            handleSaveMemo = {},
            handleDeleteMemo = {},
            contents = memo.contents.map { it.convertToContentBlockModel() },
        )
    }
}
