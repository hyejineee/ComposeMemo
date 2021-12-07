package com.example.composememoapp.util.model

import androidx.annotation.DrawableRes

data class IconModel(
    @DrawableRes val iconId: Int,
    val onClick: (() -> Unit)? = null,
    val description: String
)
