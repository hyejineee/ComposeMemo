package com.example.composememoapp.presentation.ui.component

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.util.toPx

@Composable
fun BottomBar(
    handleClickAddMemoButton: () -> Unit,
    handleClickFavoriteFilterButton: () -> Unit,
    isFavoriteFilter: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0x00000000), Color.White),
                    startY = 0.dp.toPx(),
                    endY = 100.dp.toPx()
                )
            )
    ) {


        val favoriteIconModel = if (isFavoriteFilter) {
            MiniFloatingButtonModel(
                icon = ImageVector.vectorResource(id = R.drawable.ic_round_star_24),
                description = "Favorite filter icon",
                onClick =  handleClickFavoriteFilterButton
            )
        } else {
            MiniFloatingButtonModel(
                icon = ImageVector.vectorResource(id = R.drawable.ic_round_star_border_24),
                description = "all filter icon",
                onClick = handleClickFavoriteFilterButton
            )
        }

        MiniFloatingButton(
            model = favoriteIconModel,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 16.dp)
                .size(35.dp),
            tint = MaterialTheme.colors.primary
        )

        FloatingActionButton(
            backgroundColor = MaterialTheme.colors.primaryVariant,
            onClick = handleClickAddMemoButton,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 16.dp)
        ) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = "add memo")
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun BottomBarPreview() {
    ComposeMemoAppTheme {
        BottomBar(
            handleClickAddMemoButton = {},
            handleClickFavoriteFilterButton = {}
        )
    }
}
