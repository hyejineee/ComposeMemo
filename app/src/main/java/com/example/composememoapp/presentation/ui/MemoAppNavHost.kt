package com.example.composememoapp.presentation.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MemoAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = MemoAppScreen.Home.name
    ) {

        composable(MemoAppScreen.Home.name) {
            Text(text = "Home")
        }

        composable(MemoAppScreen.Write.name) {
            Text(text = "Write")
        }
    }
}
