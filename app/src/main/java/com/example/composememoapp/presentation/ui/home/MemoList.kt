package com.example.composememoapp.presentation.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.R
import com.example.composememoapp.data.ContentBlock
import com.example.composememoapp.data.ContentType
import com.example.composememoapp.data.ImageBlock
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.component.StaggeredGridColumn

@Composable
fun MemoList(
    memos: List<MemoEntity>,
    onItemClick: (MemoEntity) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        StaggeredGridColumn(modifier = modifier.padding(horizontal = 20.dp)) {
            for (memo in memos) {
                val hasImage = memo.contents.find { it.type == ContentType.Image }
                MemoListItem(
                    memo = memo,
                    modifier = Modifier.clickable { onItemClick(memo) },
                    imageBlock = hasImage?.let {
                        it.convertToContentBlockModel() as ImageBlock
                    }
                )
            }
        }
    }
}

@Composable
fun MemoListItem(
    memo: MemoEntity,
    imageBlock: ImageBlock? = null,
    modifier: Modifier = Modifier
) {
    androidx.compose.material.Surface(
        elevation = 6.dp,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .heightIn(50.dp, 250.dp)
            .padding(4.dp),
        contentColor = if(imageBlock!=null) Color.White else Color.Black
    ) {

        imageBlock?.let {

            it.drawOnlyReadContent(modifier = Modifier)
        }

        Box() {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterStart)

            ) {
                memo.contents
                    .map { it.convertToContentBlockModel() }
                    .forEachIndexed { i: Int, block: ContentBlock<*> ->
                        when (block) {
                            is TextBlock -> block.drawOnlyReadContent(
                                modifier = if (i == 0) Modifier.padding(
                                    end = 30.dp
                                ) else Modifier
                            )
                        }
                    }
            }

            val iconVector = if (memo.isBookMarked) {
                ImageVector.vectorResource(id = R.drawable.ic_round_star_24)
            } else {
                ImageVector.vectorResource(id = R.drawable.ic_round_star_border_24)
            }

            Icon(
                imageVector = iconVector,
                contentDescription = "isFavoriteMemo",
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.TopEnd),
                tint = if(imageBlock!=null) Color.White else MaterialTheme.colors.primary
            )
        }
    }
}

@Preview
@Composable
fun MemoListItemPreview() {
    ComposeMemoAppTheme() {
        val memo = MemoEntity(
            id = 1,
            contents = listOf(
                ContentBlockEntity(
                    type = ContentType.Text,
                    seq = 1L,
                    content = "content://com.android.providers.media.documents/document/image%3A19"
                ),
                ContentBlockEntity(type = ContentType.Text, seq = 2L, content = "adskfeiwnocono"),
            ),
        )
        MemoListItem(memo = memo)
    }
}

@Preview
@Composable
fun MemoListItemImagePreview() {
    ComposeMemoAppTheme() {
        val memo = MemoEntity(
            id = 1,
            contents = listOf(
                ContentBlockEntity(type = ContentType.Image, seq = 1L, content = "adskfeiwnocono"),
                ContentBlockEntity(type = ContentType.Text, seq = 2L, content = "adskfeiwnocono"),
            ),
        )
        MemoListItem(memo = memo)
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun MemoListPreview() {
    ComposeMemoAppTheme() {
        val memos = List(10) {
            MemoEntity(
                id = it.toLong(),
                contents = List(5) { seq ->
                    ContentBlockEntity(
                        type = ContentType.Text,
                        seq = seq.toLong(),
                        content = "contentcontentcontentcontentcontentcontentcontent $seq"
                    )
                }
            )
        }
        MemoList(memos = memos, onItemClick = {})
    }
}
