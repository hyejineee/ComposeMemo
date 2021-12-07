package com.example.composememoapp.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme

@ExperimentalAnimationApi
@Composable
fun MemoApp() {
    val navController = rememberNavController()
    val lazyListState = rememberLazyListState()

    val handleClickAddMemoButton = {
        navController.navigate(MemoAppScreen.Write.name)
    }

    ComposeMemoAppTheme() {
        Scaffold(
            floatingActionButton = {
                HomeAddMemoFAB(
                    extended = lazyListState.isScrollInProgress,
                    onClick = handleClickAddMemoButton
                )
            }
        ) {
            MemoAppNavHost(
                navController = navController,
                modifier = Modifier
                    .padding(it)
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun HomeAddMemoFAB(
    extended: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    FloatingActionButton(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        onClick = onClick
    ) {
        Row(modifier = modifier.padding(20.dp)) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.addMemo)
            )

            AnimatedVisibility(visible = extended) {
                Spacer(modifier = modifier.padding(2.dp))

                Text(
                    text = stringResource(id = R.string.addMemo),
                    modifier = modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun MemoAppPreview() {
    MemoApp()
}

@ExperimentalAnimationApi
@Preview
@Composable
fun HomeAddMemoFAPPreview() {
    ComposeMemoAppTheme {
        HomeAddMemoFAB(onClick = {})
    }
}
