package com.example.composememoapp.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.composememoapp.util.model.InputState
import kotlinx.android.parcel.Parcelize

interface ContentBlock<T> {
    var seq: Int
    var content: T

    @Composable
    fun drawOnlyReadContent(modifier: Modifier)

    @Composable
    fun drawEditableContent(state: InputState, modifier:Modifier)
}
