package com.example.composememoapp.viewModel

import android.net.Uri
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.presentation.ui.component.CheckBoxBlock
import com.example.composememoapp.presentation.ui.component.CheckBoxModel
import com.example.composememoapp.presentation.ui.component.blocks.ContentType
import com.example.composememoapp.presentation.ui.component.blocks.ImageBlock
import com.example.composememoapp.presentation.ui.component.blocks.TextBlock
import com.example.composememoapp.presentation.viewModel.ContentBlockViewModel
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class ContentBlockViewModelTest {

    @Test
    @DisplayName("생성시 생성자에 들어온 값을 obseavable에 발행한다.")
    fun initWithNotEmptyListTest() {

        val list = List(5) {
            ContentBlockEntity(type = ContentType.Text, content = "hello", seq = it.toLong())
        }
        val contentBlockViewModel = ContentBlockViewModel(list)

        contentBlockViewModel.contentBlocks.test().awaitCount(1)
            .assertValue(list.map { it.convertToContentBlockModel() })
    }

    @Test
    @DisplayName("생성시 생성자의 값이 빈 리스트일 경우 textBlock 하나를 포함한 리스트를 발행한다.")
    fun initWithEmptyListTest() {
        val contentBlockViewModel = ContentBlockViewModel(emptyList())

        contentBlockViewModel.contentBlocks.test().awaitCount(2)
            .assertValue(listOf(TextBlock(content = "")))
    }

    @Test
    @DisplayName("텍스트 블록을 추가한다.")
    fun insertTextBlockTest() {
        val contentBlockViewModel = ContentBlockViewModel(emptyList())

        contentBlockViewModel.insertTextBlock()

        contentBlockViewModel.contentBlocks.test().awaitCount(3)
            .assertValue(listOf(TextBlock(content = ""), TextBlock(content = "")))
    }

    @Test
    @DisplayName("체크박스 블록을 추가한다.")
    fun insertCheckBoxBlockTest() {
        val contentBlockViewModel = ContentBlockViewModel(emptyList())

        contentBlockViewModel.insertCheckBoxBlock()

        contentBlockViewModel.contentBlocks.test().awaitCount(3).assertValue(
            listOf(
                TextBlock(content = ""),
                CheckBoxBlock(content = CheckBoxModel(text = "", isChecked = false))
            )
        )
    }

    @Test
    @DisplayName("이미지 블록을 추가한다.")
    fun insertImageBlockTest() {
        val uri = Mockito.mock(Uri::class.java)

        val contentBlockViewModel = ContentBlockViewModel(emptyList())
        contentBlockViewModel.insertImageBlock(uri = uri)

        contentBlockViewModel.contentBlocks.test().awaitCount(3).assertValue(
            listOf(
                TextBlock(content = ""),
                ImageBlock(content = uri)
            )
        )
    }

    @Test
    @DisplayName("텍스트 블록을 리스트의 중간에 추가한다.")
    fun insertTextBlockBetweenTest() {
        val contentBlockViewModel = ContentBlockViewModel(emptyList())
        contentBlockViewModel.insertTextBlock()

        contentBlockViewModel.insertTextBlock(1)

        contentBlockViewModel.contentBlocks.test().awaitCount(4).assertValue(
            listOf(
                TextBlock(content = ""),
                TextBlock(content = ""),
                TextBlock(content = ""),
            )
        )
    }

    @Test
    @DisplayName("체크박스 블록을 리스트의 중간에 추가한다.")
    fun insertCheckBoxBlockBetweenTest() {
        val contentBlockViewModel = ContentBlockViewModel(emptyList())
        contentBlockViewModel.insertTextBlock()

        contentBlockViewModel.insertCheckBoxBlock(1)

        contentBlockViewModel.contentBlocks.test().awaitCount(4).assertValue(
            listOf(
                TextBlock(content = ""),
                CheckBoxBlock(content = CheckBoxModel(text = "", isChecked = false)),
                TextBlock(content = ""),
            )
        )
    }

    @Test
    @DisplayName("이미지 블록을 리스트의 중간에 추가한다.")
    fun insertImageBlockBetweenTest() {
        val uri = Mockito.mock(Uri::class.java)

        val contentBlockViewModel = ContentBlockViewModel(emptyList())
        contentBlockViewModel.insertTextBlock()

        contentBlockViewModel.insertImageBlock(1, uri)

        contentBlockViewModel.contentBlocks.test().awaitCount(4).assertValue(
            listOf(
                TextBlock(content = ""),
                ImageBlock(content = uri),
                TextBlock(content = ""),
            )
        )
    }

    @Test
    @DisplayName("컨텐트 블록을 삭제한다.")
    fun deleteContentBlockTest() {
        val contentBlockViewModel = ContentBlockViewModel(emptyList())
        contentBlockViewModel.insertCheckBoxBlock()

        val deletedBlock = contentBlockViewModel.contentBlocks.test().values().last().last()

        contentBlockViewModel.deleteBlock(deletedBlock)

        contentBlockViewModel.contentBlocks.test().awaitCount(4).assertValue(
            listOf(
                TextBlock(content = ""),
            )
        )
    }
}
