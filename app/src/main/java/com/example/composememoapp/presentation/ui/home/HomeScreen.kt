package com.example.composememoapp.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.R
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.database.entity.TagEntity
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.HomeScreenBottomBar
import com.example.composememoapp.presentation.viewModel.MemoState
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.presentation.viewModel.TagViewModel
import com.example.composememoapp.util.fontDimensionResource
import com.example.composememoapp.util.model.rememberTextInputState

@Composable
fun HomeScreen(
    memoViewModel: MemoViewModel,
    tagViewModel: TagViewModel,
    handleClickAddMemoButton: () -> Unit,
    handleClickMemoItem: (MemoEntity) -> Unit
) {

    val memoList by memoViewModel.memoList.subscribeAsState(initial = emptyList())
    val tagList by tagViewModel.tagList.subscribeAsState(initial = emptyList())
    val state by memoViewModel.state.subscribeAsState(initial = MemoState.Loading)

    val handleChangeSearchInput = { text: String ->
        memoViewModel.searchMemo(text)
    }

    val handleChangeSelectedTag = { tag: TagEntity ->
        memoViewModel.filterMemoByTag(tag = tag.tag)
    }

    val handleClickFavoriteFilterButton = { isFavorite: Boolean ->
        memoViewModel.filterMemoByFavorite(isFavorite = isFavorite)
    }

    val snackBarMessage = when (state) {
        is MemoState.Error -> "작업 실패 : ${(state as MemoState.Error).message}"
        else -> {
            null
        }
    }

    val isLoading = when (state) {
        is MemoState.Loading -> true
        else -> false
    }

    HomeScreenContent(
        memoList = memoList,
        tagList = listOf(TagEntity(tag = "ALL")) + tagList,
        snackBarMessage = snackBarMessage,
        isLoading = isLoading,
        handleClickFavoriteFilterButton = handleClickFavoriteFilterButton,
        handleChangeSelectedTag = handleChangeSelectedTag,
        handleChangeSearchInput = handleChangeSearchInput,
        handleClickAddMemoButton = handleClickAddMemoButton,
        handleClickMemoItem = handleClickMemoItem
    )
}

@Composable
fun HomeScreenContent(
    handleChangeSearchInput: (String) -> Unit,
    memoList: List<MemoEntity>,
    tagList: List<TagEntity>,
    snackBarMessage: String?,
    isLoading: Boolean = false,
    handleChangeSelectedTag: (TagEntity) -> Unit,
    handleClickAddMemoButton: () -> Unit,
    handleClickMemoItem: (MemoEntity) -> Unit,
    handleClickFavoriteFilterButton: (Boolean) -> Unit
) {

    val searchTextInputState = rememberTextInputState(initialText = "")
    handleChangeSearchInput(searchTextInputState.text)

    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_default))
        ) {
            Text(
                text = stringResource(id = R.string.homeTitle),
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = fontDimensionResource(id = R.dimen.textsize_extra_main_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_default))
            )

            if (isLoading.not()) {
                SearchMemoTextInput(
                    state = searchTextInputState,
                    modifier = Modifier
                        .fillMaxWidth()

                )

                var selectedCategory by rememberSaveable { mutableStateOf(TagEntity(tag = "ALL")) }
                var listState = rememberLazyListState()

                CategoryMenuBar(
                    categories = tagList,
                    onClick = {
                        handleChangeSelectedTag(it)
                        selectedCategory = it
                    },
                    selected = selectedCategory,
                    listState = listState,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
                    prefix = "#"
                )

                MemosListScreen(
                    memos = memoList,
                    onItemClick = handleClickMemoItem,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }

        var isFavoriteFilter by rememberSaveable { mutableStateOf(false) }

        HomeScreenBottomBar(
            isFavoriteFilter = isFavoriteFilter,
            handleClickFavoriteFilterButton = {
                isFavoriteFilter = !isFavoriteFilter
                handleClickFavoriteFilterButton(isFavoriteFilter)
            },
            handleClickAddMemoButton = handleClickAddMemoButton,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )

        snackBarMessage?.let {
            LaunchedEffect(key1 = snackBarMessage) {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
            }
        }

        SnackbarHost(hostState = snackbarHostState, Modifier.align(Alignment.BottomCenter))
    }
}

@Preview
@Composable
fun HomeScreenPreview() {

    ComposeMemoAppTheme {
        HomeScreenContent(
            memoList = emptyList(),
            snackBarMessage = "hello",
            handleChangeSearchInput = {},
            handleClickAddMemoButton = {},
            handleClickMemoItem = {},
            handleChangeSelectedTag = {},
            handleClickFavoriteFilterButton = {},
            tagList = emptyList()
        )
    }
}
