package com.example.composememoapp.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.toPx() = with(LocalDensity.current) { toPx() }

fun dpToPx(context: Context, dp: Int) = (dp * context.resources.displayMetrics.density).toInt()
