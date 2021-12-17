package com.example.composememoapp.presentation.ui.detailandwrite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.Chip
import com.example.composememoapp.presentation.ui.component.StaggeredGridRow
import com.example.composememoapp.util.model.rememberTextInputState

@Composable
fun TagScreen(
    tagList:List<String>,
    modifier: Modifier = Modifier
){
    val textState = rememberTextInputState(initialText = "")
    val handleClickAddTag = {
    }

    Column(
        modifier = modifier
    ) {
        TagTextInput(state = textState, handleClickAddTag = handleClickAddTag)

        StaggeredGridRow(
            itemsSize = tagList.size,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            for (tag in tagList){
                Chip(text = "#${tag}")
            }
        }
    }
}

@Preview
@Composable
fun TagScreenPreview(){
    val tagList = List(20){
        "Tag $it"
    }
    ComposeMemoAppTheme {
        TagScreen(tagList = tagList)
    }
}