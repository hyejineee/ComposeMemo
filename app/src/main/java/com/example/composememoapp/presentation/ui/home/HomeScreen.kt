package com.example.composememoapp.presentation.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composememoapp.R
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.database.entity.TagEntity
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.BottomBar
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.presentation.viewModel.TagViewModel
import com.example.composememoapp.util.model.TagListState
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

    val handleChangeSearchInput = { text: String ->
        memoViewModel.searchMemo(text)
    }

    val handleChangeSelectedTag = { tag: TagEntity ->
        memoViewModel.filterMemoByTag(tag = tag.tag)
    }

    HomeScreenContent(
        memoList = memoList,
        tagList = listOf(TagEntity(tag = "ALL")) + tagList,
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
    handleChangeSelectedTag: (TagEntity) -> Unit,
    handleClickAddMemoButton: () -> Unit,
    handleClickMemoItem: (MemoEntity) -> Unit
) {

    val searchTextInputState = rememberTextInputState(initialText = "")
    handleChangeSearchInput(searchTextInputState.text)

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.White, Color(0x000000000)),
                    )
                )
                .padding(bottom = 50.dp)
        ) {
            Text(
                text = stringResource(id = R.string.homeTitle),
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 40.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )

            SearchMemoTextInput(
                state = searchTextInputState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
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
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
            )

            MemosListScreen(
                memos = memoList,
                onItemClick = handleClickMemoItem,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }

        BottomBar(
            handleClickAddMemoButton = handleClickAddMemoButton,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {

    ComposeMemoAppTheme {
//        HomeScreenContent(memoList = emptyList(), {}, {})
    }
}
