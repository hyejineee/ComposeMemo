package com.example.composememoapp.util

import android.content.Context
import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

@Composable
fun Dp.toPx() = with(LocalDensity.current) { toPx() }

// https://stackoverflow.com/questions/67522145/text-composable-dimensionresource-not-working-as-fontsize-parameter
@Composable
@ReadOnlyComposable
fun fontDimensionResource(@DimenRes id:Int) = dimensionResource(id = id).value.sp

fun dpToPx(context: Context, dp: Int) = (dp * context.resources.displayMetrics.density).toInt()
