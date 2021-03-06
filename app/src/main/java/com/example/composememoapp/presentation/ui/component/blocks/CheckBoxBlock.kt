package com.example.composememoapp.presentation.ui.component

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.presentation.ui.component.blocks.ContentBlock
import com.example.composememoapp.presentation.ui.component.blocks.ContentType
import com.example.composememoapp.presentation.viewModel.ContentBlockViewModel
import com.google.gson.Gson
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.RawValue

data class CheckBoxModel(
    var text: String,
    var isChecked: Boolean
)

@kotlinx.parcelize.Parcelize
class CheckBoxBlock(
    override val seq: Long = 0,
    override val content: @RawValue CheckBoxModel,
) : ContentBlock<CheckBoxModel>(), Parcelable {

    @IgnoredOnParcel
    var checkState: MutableState<Boolean> = mutableStateOf(content.isChecked)

    @IgnoredOnParcel
    var textInputState: MutableState<TextFieldValue> = mutableStateOf(
        TextFieldValue(
            text = content.text,
            selection = TextRange(content.text.length)
        )
    )

    @Composable
    override fun drawOnlyReadContent(
        modifier: androidx.compose.ui.Modifier,
    ) {
        Row(modifier = modifier) {
            Checkbox(
                checked = content.isChecked,
                onCheckedChange = {},
                colors = CheckboxDefaults.colors(
                    uncheckedColor = MaterialTheme.colors.secondary
                )

            )
            Spacer(modifier = Modifier.width(10.dp))

            Text(text = content.text, fontSize = 13.sp)
        }
    }

    @Composable
    override fun drawEditableContent(
        modifier: androidx.compose.ui.Modifier,
        viewModel: ContentBlockViewModel
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = checkState.value,
                onCheckedChange = {
                    checkState.value = checkState.value.not()
                    content.isChecked = checkState.value
                }
            )
            Spacer(modifier = Modifier.width(10.dp))

            TextInput(
                value = textInputState.value,
                onValueChange = {
                    textInputState.value = it
                    content.text = it.text
                },
                modifier = modifier
                    .fillMaxWidth(),
                singleLine = true,
                keyBoardActions = KeyboardActions(onNext = { addNextBlock(viewModel) }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
        }
    }

    override fun convertToContentBlockEntity() = ContentBlockEntity(
        type = ContentType.CheckBox,
        seq = seq,
        content = Gson().toJson(content)
    )

    companion object : Parceler<CheckBoxBlock> {
        override fun CheckBoxBlock.write(dest: Parcel, flags: Int) {
            dest?.writeLong(seq)
            dest?.writeString(content.text)
            dest?.writeByte(if (content.isChecked) 1 else 0)
        }

        override fun create(parcel: Parcel): CheckBoxBlock = CheckBoxBlock(
            parcel.readLong(),
            CheckBoxModel(parcel.readString() ?: "", parcel.readByte() != 0.toByte())
        )
    }

    override fun isEmpty(): Boolean = textInputState.value.text.isBlank()

    override fun addNextBlock(viewModel: ContentBlockViewModel) {

        viewModel.insertBlock(CheckBoxBlock(content = CheckBoxModel("", false)))
    }
}
