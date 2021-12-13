package com.example.composememoapp.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.composememoapp.R
import com.example.composememoapp.data.MemoEntity
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.MemoAppScreen
import com.example.composememoapp.util.model.rememberTextInputState
import com.example.composememoapp.util.toPx

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
) {

    val handleClickAddMemoButton = {
        navController.navigate(MemoAppScreen.Write.name)
    }

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

            val memoList = List(10) {

                if (it == 0 || it==4 || it ==5) {
                    MemoEntity(
                        id = it,
                        contents = List(11) { s ->
                            TextBlock(seq = s, content = "content$s")
                        }
                    )
                } else {
                    MemoEntity(
                        id = it,
                        contents = List(5) { s ->
                            TextBlock(seq = s, content = "content$s")
                        }
                    )
                }
            }

            MemosScreen(
                memos = memoList,
                onItemClick = {
                    navController.navigate("${MemoAppScreen.Detail.name}/${it.id}")
                },
                modifier = Modifier.padding(vertical = 10.dp)
            )

        }

        HomeBottomBar(
            handleClickAddMemoButton = handleClickAddMemoButton,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )

    }
}

@Composable
fun HomeBottomBar(
    handleClickAddMemoButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0x00000000), Color.White),
                    startY = 0.dp.toPx(),
                    endY = 100.dp.toPx()
                )
            )
    ) {
        FloatingActionButton(
            backgroundColor = MaterialTheme.colors.primaryVariant,
            onClick = handleClickAddMemoButton,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "add memo")
        }
    }

}


@Preview
@Composable
fun HomeScreenPreview() {
    ComposeMemoAppTheme {
        HomeScreen()
    }
}
