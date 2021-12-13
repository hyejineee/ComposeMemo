package com.example.composememoapp.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.example.composememoapp.presentation.ui.detailandwrite.DetailAndWriteScreen
import com.example.composememoapp.presentation.ui.home.HomeScreen
import com.example.composememoapp.presentation.viewModel.MemoViewModel

@Composable
fun MemoAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MemoViewModel = MemoViewModel()
) {

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = MemoAppScreen.Home.name
    ) {

        composable(MemoAppScreen.Home.name) {
            HomeScreen(navController = navController)
        }

        val detailScreenName = MemoAppScreen.Detail
        composable(
            route = "$detailScreenName/{${Key.MEMO_ARGS_KEY}}",
            arguments = listOf(
                navArgument(Key.MEMO_ARGS_KEY) {
                    type = NavType.IntType
                }
            ),
        ) { entry ->
            val memoId = entry.arguments?.getInt(Key.MEMO_ARGS_KEY)
            val memo = viewModel.getMemo(memoId ?: kotlin.run { return@composable })

            DetailAndWriteScreen(memoEntity = memo)
        }

        composable(MemoAppScreen.Write.name) {
            DetailAndWriteScreen()
        }
    }
}

object Key {
    const val MEMO_ARGS_KEY = "memoId"
}
