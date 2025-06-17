package com.forknowledge.core.ui.theme.state

import androidx.annotation.DrawableRes

data class FloatingAction(
    val label: String,
    @DrawableRes val icon: Int,
    val action: () -> Unit
)
