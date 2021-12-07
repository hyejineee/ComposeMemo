package com.example.composememoapp.presentation.ui.home

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.composememoapp.R
import com.example.composememoapp.data.MemoEntity
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.MemoAppScreen
import com.example.composememoapp.util.model.rememberTextInputState

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController()
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.homeTitle),
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 50.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
        )

        val searchTextInputState = rememberTextInputState(initialText = "")
        SearchMemoTextInput(
            state = searchTextInputState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp, vertical = 5.dp)
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

        val memoList = List(10) {
            MemoEntity(
                id = it,
                contents = List(5) { s ->
                    TextBlock(seq = s, contents = "content$s")
                }
            )
        }
        MemosScreen(
            memos = memoList,
            onItemClick = {
                navController.navigate(MemoAppScreen.Write.name)
            },
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    ComposeMemoAppTheme {
        HomeScreen()
    }
}
