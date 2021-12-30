package com.example.composememoapp.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme

@Composable
fun TextInput(
    hint: String? = null,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    singleLine: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                value = value,
                onValueChange = { onValueChange(it) },
                modifier = modifier
                    .fillMaxWidth(),
                singleLine = singleLine,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Start,
                    fontSize = 15.sp
                )
            )
            hint?.let {
                if ((value.text.isNotEmpty()).not()) {
                    Text(
                        text = it, fontSize = 12.sp, color = Color.LightGray,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterStart)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextInputPreview() {
    ComposeMemoAppTheme() {
//        TextInput(
//            text = "hello",
//            onValueChange = {},
//        )
    }
}
