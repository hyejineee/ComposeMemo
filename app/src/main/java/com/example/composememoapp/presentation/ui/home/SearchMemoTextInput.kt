package com.example.composememoapp.presentation.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.TextInput
import com.example.composememoapp.util.Descriptions
import com.example.composememoapp.util.model.IconModel
import com.example.composememoapp.util.model.TextInputSate
import com.example.composememoapp.util.model.rememberTextInputState

@Composable
fun SearchMemoTextInput(
    state: TextInputSate = rememberTextInputState(""),
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
        TextInput(
            text = state.text,
            onValueChange = {
                if (state.isHint) {
                    state.text = it.replace(state.hint!!, "")
                }else{
                    state.text = it
                }

                if(it.isBlank()){
                    state.text = state.hint!!
                }

            },
            modifier = Modifier
                .padding(5.dp),
            iconModel = iconModel,
            clickableIconModel = clickableIconModel
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchMemoTextInput() {
    ComposeMemoAppTheme {
        val hint = stringResource(id = R.string.putSearchWordCaption)
        val state = TextInputSate(hint = hint, initialText = hint)
        SearchMemoTextInput(state = state)
    }
}
