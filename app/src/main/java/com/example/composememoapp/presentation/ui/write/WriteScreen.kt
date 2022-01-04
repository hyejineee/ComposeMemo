package com.example.composememoapp.presentation.ui.write

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import com.example.composememoapp.data.MemoModel
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.WriteScreenBottomBar
import com.example.composememoapp.presentation.ui.component.WriteScreenTopAppBar
import com.example.composememoapp.presentation.ui.component.blocks.ContentBlock
import com.example.composememoapp.presentation.ui.component.blocks.ContentType
import com.example.composememoapp.presentation.viewModel.ContentBlockViewModel
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.presentation.viewModel.TagViewModel
import com.example.composememoapp.util.model.rememberTagListState

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun WriteScreen(
    memoEntity: MemoEntity? = null,
    tagViewModel: TagViewModel,
    memoViewModel: MemoViewModel,
    contentBlockViewModel: ContentBlockViewModel,
    handleBackButtonClick: () -> Unit,
) {

    val context = LocalContext.current

    val allTag by tagViewModel.tagList.subscribeAsState(initial = emptyList())

    val contentsState =
        contentBlockViewModel.contentBlocks.subscribeAsState(initial = emptyList())

    val tagState = rememberTagListState(
        initialContents = memoEntity?.tagEntities ?: listOf()
    )

    var favoriteSate = rememberSaveable { mutableStateOf(memoEntity?.isBookMarked ?: false) }

    val handleSaveMemo = {
        val newMemoModel = memoEntity?.let {
            it.copy().convertToMemoModel().apply {
                contents = contentsState.value
                tagEntities = tagState.tags
            }
        } ?: MemoModel(contents = contentsState.value, tagEntities = tagState.tags)

        val contentsCount = contentsState.value.count {
            it.content.toString().isNotBlank() or it.content.toString().isNotEmpty()
        }

        if (contentsCount > 0) {
            memoViewModel.saveMemo(memoModel = newMemoModel, context = context)
        }
    }

    val handleDeleteMemo = {
        memoEntity?.let {
            memoViewModel.deleteMemo(it)
        }
        handleBackButtonClick()
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

    val handleAddTag: (String) -> Unit = { s: String ->
        if (s !in tagState.tags) {
            tagState.tags = tagState.tags.plus(s)
        }
    }

    Log.d("Write", "content : ${contentsState}")


    val handleClickFavoriteButton = {
        favoriteSate.value = !favoriteSate.value
        memoEntity?.isBookMarked = favoriteSate.value
    }


    WriteScreenContent(
        memoEntity = memoEntity?.convertToMemoModel(),
        allTag = allTag.map { it.tag },
        contents = contentsState.value,
        tagList = tagState.tags,
        isFavorite = favoriteSate.value,
        handleDeleteMemo = handleDeleteMemo,
        handleBackButtonClick = handleBackButtonClick,
        handleSaveMemo = handleSaveMemo,
        handleClickFavoriteButton = handleClickFavoriteButton,
        handleAddDefaultBlock = handleAddDefaultBlock,
        handleAddTag = handleAddTag,
        handleAddImageBlock = handleAddImageBlock,
        handleAddCheckBoxBlock = handleAddCheckBoxBlock
    )
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun WriteScreenContent(
    memoEntity: MemoModel?,
    tagList: List<String>,
    allTag: List<String>,
    contents: List<ContentBlock<*>>,
    isFavorite : Boolean,
    handleDeleteMemo: () -> Unit,
    handleBackButtonClick: () -> Unit,
    handleSaveMemo: () -> Unit,
    handleClickFavoriteButton : ()->Unit,
    handleAddTag: (String) -> Unit,

    handleAddDefaultBlock: (Int?) -> Unit,
    handleAddImageBlock: (Int?) -> (Uri?) -> Unit,
    handleAddCheckBoxBlock: (Int?) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var index by rememberSaveable { mutableStateOf<Int?>(null) }

    val scrollState = rememberScrollState()

    val handleClickBackButton = {
        handleSaveMemo()
        handleBackButtonClick()
    }

    BackHandler() {
        handleClickBackButton()
    }

    val handleCursorPosition = { i: Int ->
        index = i
    }

    Scaffold(
        topBar = {
            TopAppBar() {
                WriteScreenTopAppBar(
                    handleClickBackButton = handleClickBackButton,
                    handleClickFavoriteButton = handleClickFavoriteButton,
                    handleClickDeleteButton = handleDeleteMemo,
                    isFavorite = isFavorite,
                    showMenuIcon = memoEntity != null
                )
            }
        },
        bottomBar = {
            WriteScreenBottomBar(
                handleAddImage = handleAddImageBlock(index),
                handleAddCheckBox = { handleAddCheckBoxBlock(index) }
            )
        },
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
        ) {
            TagScreen(
                tagList = tagList,
                allTag = allTag,
                handleClickAddTag = handleAddTag,
                modifier = Modifier
                    .fillMaxSize()
            )

            ContentBlocks(
                contents = contents,
                focusRequester = focusRequester,
                focusedIndex = index,
                keyboardController = keyboardController,
                handleCursorPosition = handleCursorPosition,
                handleAddDefaultBlock = handleAddDefaultBlock
            )
        }
    }
}

@ExperimentalAnimationApi
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

        val content = memo.contents.map { it.convertToContentBlockModel() }
        WriteScreenContent(
            memoEntity = memo.convertToMemoModel(),
            allTag = listOf(),
            tagList = listOf(),
            isFavorite = false,
            handleAddTag = {},
            handleBackButtonClick = {},
            handleAddDefaultBlock = { },
            handleSaveMemo = {},
            handleDeleteMemo = {},
            handleAddImageBlock = { {} },
            contents = content,
            handleAddCheckBoxBlock = {},
            handleClickFavoriteButton = {}
        )
    }
}
