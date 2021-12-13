package com.example.composememoapp.presentation.ui.detailandwrite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.data.entity.MemoEntity
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.util.model.rememberTextInputState

@Composable
fun DetailAndWriteScreen(
    memoEntity: MemoEntity? = null,
    viewModel: MemoViewModel = MemoViewModel()
) {

    Scaffold(
        topBar = {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        },
        modifier = Modifier
            .fillMaxSize()
    ) {

        memoEntity?.let {
            MemoDetailScreen(memoEntity = it)
        } ?: run {
            MemoCreateScreen()
        }
    }
}

@Composable
fun MemoDetailScreen(memoEntity: MemoEntity) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(memoEntity.contents) {
            val textInputState = rememberTextInputState(initialText = it.content.toString())
            it.drawEditableContent(state = textInputState)
        }
    }
}

@Composable
fun MemoCreateScreen() {
    val state = rememberTextInputState(initialText = "")
    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        BasicTextField(value = state.text, onValueChange = { state.text = it }, modifier = Modifier.fillMaxWidth().focusTarget())
    }
}

@Preview(showBackground = true)
@Composable
fun DetailAndWriteScreenPreview() {
    ComposeMemoAppTheme() {
        val memo = MemoEntity(
            id = 0,
            contents = List(10) {
                TextBlock(
                    seq = it,
                    content = "this is text block content $it this is text block content $it this is text block content $it"
                )
            }
        )
        DetailAndWriteScreen(memoEntity = memo)
    }
}

@Preview(showBackground = true)
@Composable
fun MemoCreateScreenPreview() {
    ComposeMemoAppTheme {
        MemoCreateScreen()
    }
}
