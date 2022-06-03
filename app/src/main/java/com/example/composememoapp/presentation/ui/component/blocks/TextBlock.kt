package com.example.composememoapp.presentation.ui.component.blocks

import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.presentation.ui.component.TextInput
import com.example.composememoapp.presentation.viewModel.ContentBlockViewModel
import kotlinx.parcelize.IgnoredOnParcel

@kotlinx.parcelize.Parcelize
data class TextBlock(
    override var seq: Long = 0,
    override var content: String,
) : ContentBlock<String>(), Parcelable {

    @IgnoredOnParcel
    var textInputState: MutableState<TextFieldValue> =
        mutableStateOf(TextFieldValue(text = content, selection = TextRange(content.length)))

    @Composable
    override fun drawOnlyReadContent(modifier: androidx.compose.ui.Modifier) {
        Box(modifier = modifier) {
            Text(text = content, fontSize = 13.sp)
        }
    }

    @Composable
    override fun drawEditableContent(
        modifier: androidx.compose.ui.Modifier,
        viewModel: ContentBlockViewModel
    ) {
        val focusManager = LocalFocusManager.current
        TextInput(
            value = textInputState.value,
            onValueChange = {
                textInputState.value = it
                content = it.text
            },
            modifier = modifier
                .fillMaxWidth(),
            singleLine = true,
            keyBoardActions = KeyboardActions(
                onNext = {
                    addNextBlock(viewModel = viewModel)
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
    }

    override fun convertToContentBlockEntity() = ContentBlockEntity(
        type = ContentType.Text,
        seq = seq,
        content = textInputState.value.text
    )

    override fun isEmpty(): Boolean {
        Log.d("TextBlock", textInputState.value.text)
        return textInputState.value.text.isBlank()
    }
    override fun addNextBlock(viewModel: ContentBlockViewModel) {
        viewModel.insertBlock(TextBlock(content = ""))
    }
}
