package com.example.composememoapp.presentation.ui.write

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.util.model.rememberTextInputState

@Composable
fun TagScreen(
    tagList: List<String>,
    allTag: List<String> = emptyList(),
    handleClickAddTag: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val textState = rememberTextInputState(initialText = "")

    Column(
        modifier = modifier
    ) {
        TagTextInput(
            state = textState,
            handleClickAddTag = handleClickAddTag,
            tagList = allTag
        )
        TagList(tagList = tagList)
    }
}

@Preview
@Composable
fun TagScreenPreview() {
    val tagList = List(20) {
        "Tag $it"
    }
    ComposeMemoAppTheme {
        TagScreen(tagList = tagList, handleClickAddTag = {})
    }
}
