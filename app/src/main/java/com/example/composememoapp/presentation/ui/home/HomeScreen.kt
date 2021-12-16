package com.example.composememoapp.presentation.ui.home

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.composememoapp.R
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.BottomBar
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.util.model.rememberTextInputState

@Composable
fun HomeScreen(
    memoViewModel: MemoViewModel,
    handleClickAddMemoButton: () -> Unit,
    handleClickMemoItem: (MemoEntity) -> Unit
) {
    val memoList by memoViewModel.memoList.subscribeAsState(initial = emptyList())

    HomeScreenContent(
        memoList = memoList,
        handleClickAddMemoButton = handleClickAddMemoButton,
        handleClickMemoItem = handleClickMemoItem
    )
}


@Composable
fun HomeScreenContent(
    memoList: List<MemoEntity>,
    handleClickAddMemoButton: () -> Unit,
    handleClickMemoItem: (MemoEntity) -> Unit
) {

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

            val searchTextInputState = rememberTextInputState(initialText = "")
            SearchMemoTextInput(
                state = searchTextInputState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
            )

            var selectedCategory by rememberSaveable { mutableStateOf("ALL") }
            var listState = rememberLazyListState()
            val categories = listOf(
                "ALL",
                "#category1",
                "#category2",
                "#category3",
                "#category4",
                "#category5",
                "#category6"
            )

            CategoryMenuBar(
                categories = categories,
                onClick = { selectedCategory = it },
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
        HomeScreenContent(memoList = emptyList(), {}, {})
    }
}
