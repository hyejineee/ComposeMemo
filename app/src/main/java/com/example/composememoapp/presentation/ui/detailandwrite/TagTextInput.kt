package com.example.composememoapp.presentation.ui.detailandwrite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.TextInput
import com.example.composememoapp.util.Descriptions
import com.example.composememoapp.util.model.IconModel
import com.example.composememoapp.util.model.TextInputSate
import java.util.regex.Pattern

@Composable
fun TagTextInput(
    state: TextInputSate,
    handleClickAddTag: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val clickableIconModel = IconModel(
        iconId = R.drawable.ic_round_add_circle_outline_24,
        onClick = {
            handleClickAddTag(state.text)
            state.text = ""
        },
        description = Descriptions.ClearIcon.text
    )

    Column() {
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
                    state.text = it
                },
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                clickableIconModel = clickableIconModel,
                hint = stringResource(id = R.string.addTagCaption),
                singleLine = true
            )
        }

        val regex = "^[a-zA-Z0-9]+$"
        if (!Pattern.matches(regex, state.text) && state.text.isNotBlank()) {
            state.text = state.text.dropLast(1)
            Text(
                text = "특수문자를 입력할 수 없습니다.",
                fontSize = 10.sp,
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchMemoTextInput() {
    ComposeMemoAppTheme {
        val state = TextInputSate("hello^")
        TagTextInput(
            state = state,
            handleClickAddTag = {}
        )
    }
}
