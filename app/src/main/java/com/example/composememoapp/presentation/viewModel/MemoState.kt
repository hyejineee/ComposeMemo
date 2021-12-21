package com.example.composememoapp.presentation.viewModel

sealed class MemoState {
    object Loading: MemoState()
    object FetchSuccess : MemoState()
    object SaveSuccess : MemoState()
    object DeleteSuccess : MemoState()
    data class Error(val message: String) : MemoState()
}
