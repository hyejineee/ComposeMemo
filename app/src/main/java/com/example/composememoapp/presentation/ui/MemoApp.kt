package com.example.composememoapp.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.compose.rememberNavController
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.util.Descriptions

@Composable
fun MemoApp() {
    val navController = rememberNavController()

    ComposeMemoAppTheme() {
        Scaffold() {
            MemoAppNavHost(
                navController = navController,
                modifier = Modifier
                    .padding(it)
                    .semantics { contentDescription = Descriptions.MemoAppNavHost.name }
            )
        }
    }
}
