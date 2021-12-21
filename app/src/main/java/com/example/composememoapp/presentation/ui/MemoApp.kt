package com.example.composememoapp.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.presentation.viewModel.TagViewModel

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun MemoApp(
    memoViewModel: MemoViewModel = hiltViewModel(),
    tagViewModel: TagViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    ComposeMemoAppTheme() {
        Scaffold() {
            MemoAppNavHost(
                memoViewModel = memoViewModel,
                tagViewModel = tagViewModel,
                navController = navController,
                modifier = Modifier
                    .padding(it),
            )
        }
    }
}
