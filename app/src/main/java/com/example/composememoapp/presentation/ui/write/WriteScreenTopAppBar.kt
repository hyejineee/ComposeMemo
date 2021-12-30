package com.example.composememoapp.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composememoapp.R
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme

@Composable
fun WriteScreenTopAppBar(
    handleClickBackButton: () -> Unit,
    handleClickFavoriteButton: () -> Unit,
    handleClickDeleteButton: () -> Unit,
    showMenuIcon: Boolean = false,
    isFavorite: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
    ) {
        Icon(
            modifier = Modifier
                .clickable(
                    onClick = handleClickBackButton
                )
                .align(Alignment.CenterStart)
                .size(25.dp),
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Arrow Back Icon"
        )

        if (showMenuIcon) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val favoriteIcon =
                    if (isFavorite) ImageVector.vectorResource(id = R.drawable.ic_round_star_24)
                    else ImageVector.vectorResource(id = R.drawable.ic_round_star_border_24)

                Icon(
                    imageVector = favoriteIcon,
                    contentDescription = "Favorite Icon",
                    Modifier
                        .clickable(onClick = handleClickFavoriteButton)
                        .size(25.dp)

                )

                Spacer(modifier = Modifier.size(5.dp))

                Icon(
                    imageVector = Icons.TwoTone.Delete,
                    contentDescription = "Delete Icon",
                    modifier = Modifier
                        .clickable(onClick = handleClickDeleteButton)
                        .size(25.dp)

                )
            }
        }
    }
}

@Preview
@Composable
fun WriteScreenTopAppBarPreview() {
    ComposeMemoAppTheme() {
        WriteScreenTopAppBar(
            handleClickBackButton = { /*TODO*/ },
            handleClickFavoriteButton = { /*TODO*/ },
            handleClickDeleteButton = { /*TODO*/ },
            showMenuIcon = true
        )
    }
}
