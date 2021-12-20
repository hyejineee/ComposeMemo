package com.example.composememoapp.presentation.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.presentation.ui.detailandwrite.DetailAndWriteScreen
import com.example.composememoapp.presentation.ui.home.HomeScreen
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.presentation.viewModel.TagViewModel

@ExperimentalComposeUiApi
@Composable
fun MemoAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    memoViewModel: MemoViewModel,
    tagViewModel: TagViewModel
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = MemoAppScreen.Home.name
    ) {


        val handleClickAddMemoButton = {
            navController.navigate(MemoAppScreen.Write.name) {
                popUpTo(MemoAppScreen.Home.name)
            }
        }

        val handleClickMemoItem = { memo: MemoEntity ->
            navController.navigate("${MemoAppScreen.Detail.name}/${memo.id}") {
                popUpTo(MemoAppScreen.Home.name)
            }
        }

        val handleBackButtonClick = {
            navController.popBackStack()
        }

        composable(MemoAppScreen.Home.name) {
            HomeScreen(
                memoViewModel = memoViewModel,
                handleClickAddMemoButton = handleClickAddMemoButton,
                handleClickMemoItem = handleClickMemoItem,
                tagViewModel = tagViewModel
            )
        }

        val detailScreenName = MemoAppScreen.Detail

        composable(
            route = "$detailScreenName/{${Key.MEMO_ARGS_KEY}}",
            arguments = listOf(
                navArgument(Key.MEMO_ARGS_KEY) {
                    type = NavType.LongType
                }
            ),
        ) { entry ->
            val memoId = entry.arguments?.getLong(Key.MEMO_ARGS_KEY) ?: -1L
            val memo = memoViewModel.getMemo(memoId = memoId)

            DetailAndWriteScreen(
                memoViewModel = memoViewModel,
                memoEntity = memo,
                handleBackButtonClick = { handleBackButtonClick() },
            )
        }

        composable(MemoAppScreen.Write.name) {
            DetailAndWriteScreen(
                memoViewModel = memoViewModel,
                handleBackButtonClick = { handleBackButtonClick() },
            )
        }
    }
}

object Key {
    const val MEMO_ARGS_KEY = "memoId"
}
