package com.example.composememoapp.presentation.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme

@ExperimentalAnimationApi
@Composable
fun AddMemoFloatingButton(
    extended: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    FloatingActionButton(
        backgroundColor = MaterialTheme.colors.primaryVariant,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(id = R.string.addMemo),
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            AnimatedVisibility(visible = extended) {
                Spacer(modifier = Modifier.padding(2.dp))

                Text(
                    text = stringResource(id = R.string.addMemo),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun HomeAddMemoFAPPreview() {
    ComposeMemoAppTheme {
        AddMemoFloatingButton(onClick = {})
    }
}