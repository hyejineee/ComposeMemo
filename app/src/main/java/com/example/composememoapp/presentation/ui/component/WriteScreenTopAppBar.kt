package com.example.composememoapp.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.composememoapp.R

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
    ) {
        Icon(
            modifier = Modifier
                .clickable(
                    onClick = handleClickBackButton
                )
                .padding(10.dp),
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Arrow Back Icon"
        )

        if (showMenuIcon) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            ) {

                val favoriteIcon =
                    if (isFavorite) ImageVector.vectorResource(id = R.drawable.ic_round_star_24)
                    else ImageVector.vectorResource(id = R.drawable.ic_round_star_border_24)

                Icon(
                    imageVector = favoriteIcon,
                    contentDescription = "Favorite Icon",
                    Modifier.clickable(onClick = handleClickFavoriteButton)
                )

                Icon(
                    imageVector = Icons.TwoTone.Delete,
                    contentDescription = "Delete Icon",
                    modifier = Modifier
                        .clickable(onClick = handleClickDeleteButton)
                )
            }
        }
    }
}
