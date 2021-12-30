package com.example.composememoapp.presentation.ui.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.composememoapp.presentation.ui.component.DropDownList
import com.example.composememoapp.presentation.ui.component.TextInputIconable
import com.example.composememoapp.util.Descriptions
import com.example.composememoapp.util.model.IconModel
import com.example.composememoapp.util.model.TextInputSate
import java.util.regex.Pattern

@Composable
fun TagTextInput(
    state: TextInputSate,
    tagList: List<String> = emptyList(),
    handleClickAddTag: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val regex = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]+$"
    val clickableIconModel = IconModel(
        iconId = R.drawable.ic_round_add_circle_outline_24,
        onClick = {

            if (state.text.isNotBlank() && Pattern.matches(regex, state.text)) {
                handleClickAddTag(state.text)
                state.text = ""
            }
        },
        description = Descriptions.AddTagIcon.text
    )

    Column {
        androidx.compose.material.Surface(
            elevation = 5.dp,
            shape = RoundedCornerShape(50.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            TextInputIconable(
                text = state.text,
                onValueChange = {
                    state.text = it
                },
                modifier = Modifier
                    .height(35.dp)
                    .fillMaxWidth(),
                clickableIconModel = clickableIconModel,
                hint = stringResource(id = R.string.addTagCaption),
                singleLine = true,
            )
        }

        if (state.text.isNotBlank() && tagList.count { it.contains(state.text) } > 0) {
            DropDownList(
                list = tagList.filter { it.contains(state.text) },
                onClick = {
                    handleClickAddTag(it)
                    state.text = ""
                },
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
            )
        }


        if (!Pattern.matches(regex, state.text) && state.text.isNotBlank()) {

            Text(
                text = stringResource(R.string.specialCharacterCaution),
                fontSize = 12.sp,
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
            )

        }

        if (state.text.isBlank()) {
            Text(
                text = stringResource(R.string.blankTagCaution),
                fontSize = 12.sp,
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
        val state = TextInputSate("hell")
        TagTextInput(
            state = state,
            handleClickAddTag = {}
        )
    }
}
