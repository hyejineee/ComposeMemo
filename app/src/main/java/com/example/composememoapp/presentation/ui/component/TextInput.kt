package com.example.composememoapp.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.util.model.IconModel

@Composable
fun TextInput(
    iconModel: IconModel? = null,
    clickableIconModel: IconModel? = null,
    showHint: Boolean = false,
    hint: String? = null,
    text: String,
    onValueChange: (s: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.padding(horizontal = 10.dp, vertical = 3.dp)
    ) {

        iconModel?.let { it ->
            Icon(
                modifier = Modifier
                    .size(24.dp, 24.dp)
                    .weight(.5f),
                painter = painterResource(id = it.iconId),
                contentDescription = it.description
            )
            Spacer(Modifier.width(5.dp))
        }

        Box(modifier = modifier.weight(5f)) {
            BasicTextField(
                value = text,
                onValueChange = { onValueChange(it) },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp
                ),
            )

            hint?.let {
                if ((text.isNotEmpty()).not()) {
                    Text(text = it, fontSize = 12.sp, color = Color.LightGray)
                }
            }
        }

        clickableIconModel?.let { clickable ->
            Spacer(Modifier.width(5.dp))

            Icon(
                modifier = Modifier
                    .clickable(onClick = clickable.onClick?.let { it } ?: {})
                    .size(24.dp, 24.dp)
                    .weight(.5f),
                painter = painterResource(id = clickable.iconId),
                contentDescription = clickable.description,
            )
        }
    }
}

@Preview
@Composable
fun TextInputPreview() {
    ComposeMemoAppTheme() {
        TextInput(
            text = "hello",
            onValueChange = {},
            iconModel = IconModel(
                iconId = R.drawable.ic_launcher_background,
                description = "first icon"
            ),
            clickableIconModel = IconModel(
                iconId = R.drawable.ic_launcher_background,
                description = "last icon",
                onClick = {}
            )
        )
    }
}

@Preview
@Composable
fun TextInputNoIconPreview() {
    ComposeMemoAppTheme() {
        TextInput(
            text = "",
            onValueChange = {},
            clickableIconModel = IconModel(
                iconId = R.drawable.ic_launcher_background,
                description = "last icon",
                onClick = {}
            ),
            showHint = true,
            hint = "this is hint"
        )
    }
}
