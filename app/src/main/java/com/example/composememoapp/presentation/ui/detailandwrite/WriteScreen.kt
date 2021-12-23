package com.example.composememoapp.presentation.ui.detailandwrite

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.composememoapp.data.ContentBlock
import com.example.composememoapp.data.ContentType
import com.example.composememoapp.data.ImageBlock
import com.example.composememoapp.data.MemoModel
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.ContentBlocks
import com.example.composememoapp.presentation.ui.component.WriteScreenBottomBar
import com.example.composememoapp.presentation.ui.component.WriteScreenTopAppBar
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.presentation.viewModel.TagViewModel
import com.example.composememoapp.util.model.ContentBlocksState
import com.example.composememoapp.util.model.rememberContentBlocksState
import com.example.composememoapp.util.model.rememberTagListState

@ExperimentalComposeUiApi
@Composable
fun WriteScreen(
    memoEntity: MemoEntity? = null,
    tagViewModel: TagViewModel,
    memoViewModel: MemoViewModel,
    handleBackButtonClick: () -> Unit,
) {

    val context = LocalContext.current
    val allTag by tagViewModel.tagList.subscribeAsState(initial = emptyList())

    val contentsState = rememberContentBlocksState(
        initialContents = memoEntity
            ?.contents
            ?.map { it.convertToContentBlockModel() }
            ?: emptyList()
    )

    val tagState = rememberTagListState(
        initialContents = memoEntity?.tagEntities ?: listOf()
    )

    val handleSaveMemo = {
        val newMemoModel = memoEntity?.let {
            it.copy().convertToMemoViewModel().apply {
                contents = contentsState.contents.toList()
                tagEntities = tagState.tags
            }
        } ?: MemoModel(contents = contentsState.contents.toList(), tagEntities = tagState.tags)

        val contentsCount = contentsState.contents.count {
            it.content.toString().isNotBlank() or it.content.toString().isNotEmpty()
        }

        if (contentsCount > 0) {
            memoViewModel.saveMemo(memoModel = newMemoModel, context = context)
        }
    }

    val handleDeleteMemo = { memo: MemoEntity ->
        memoViewModel.deleteMemo(memo)
    }

    val handleAddDefaultBlock: () -> Unit =
        {
            val seq = if(contentsState.contents.isNotEmpty()) contentsState.contents.last().seq + 1 else 1
            contentsState.contents.add(TextBlock(seq = seq, ""))
        }

    val handleAddTag: (String) -> Unit = { s: String ->
        if (s !in tagState.tags) {
            tagState.tags = tagState.tags.plus(s)
        }
    }

    val handleAddImageBlock = { uri: Uri? ->
        val seq = if(contentsState.contents.isNotEmpty()) contentsState.contents.last().seq + 1 else 1
        contentsState.contents.add(ImageBlock(seq = seq, content = uri))
        Unit
    }

    Log.d("Write", "content : ${contentsState.contents.toList()}")

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
        handleAddTag = handleAddTag,
        handleAddImageBlock = handleAddImageBlock
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
    handleAddTag: (String) -> Unit,
    handleAddImageBlock: (Uri?) -> Unit,
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
        bottomBar = {
            WriteScreenBottomBar(
                handleClickAddImageButton = handleAddImageBlock
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = handleAddDefaultBlock)
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

        val content = memo.contents.map { it.convertToContentBlockModel() }
        DetailAndWriteScreenContent(
            memoEntity = memo,
            allTag = listOf(),
            tagList = listOf(),
            handleAddTag = {},
            handleBackButtonClick = {},
            handleAddDefaultBlock = { },
            handleSaveMemo = {},
            handleDeleteMemo = {},
            handleAddImageBlock = {},
            contents = content,
        )
    }
}
