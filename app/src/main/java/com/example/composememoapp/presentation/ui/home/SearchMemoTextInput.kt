package com.example.composememoapp.presentation.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.TextInputIconable
import com.example.composememoapp.util.Descriptions
import com.example.composememoapp.util.model.IconModel
import com.example.composememoapp.util.model.TextInputSate

@Composable
fun SearchMemoTextInput(
    state: TextInputSate,
    modifier: Modifier = Modifier
) {

    val iconModel = IconModel(
        iconId = R.drawable.ic_baseline_search_24,
        description = Descriptions.SearchIcon.text
    )

    val clickableIconModel = IconModel(
        iconId = R.drawable.ic_baseline_clear_24,
        onClick = { state.text = "" },
        description = Descriptions.ClearIcon.text
    )

    androidx.compose.material.Surface(
        elevation = 5.dp,
        shape = RoundedCornerShape(50.dp),
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        TextInputIconable(
            text = state.text,
            onValueChange = {
                state.text = it
            },
            modifier = Modifier
                .padding(5.dp),
            iconModel = iconModel,
            clickableIconModel = clickableIconModel,
            hint = stringResource(id = R.string.putSearchWordCaption),
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchMemoTextInput() {
    ComposeMemoAppTheme {
        val state = TextInputSate("")
        SearchMemoTextInput(state = state)
    }
}
