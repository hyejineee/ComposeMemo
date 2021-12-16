package com.example.composememoapp.presentation.viewModel


sealed class MemoState {
    object SaveSuccess : MemoState()
    data class Error(val message: String) : MemoState()
}
