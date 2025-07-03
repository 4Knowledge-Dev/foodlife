package com.forknowledge.feature.planner

import androidx.compose.ui.graphics.painter.Painter

data class SheetAction(
    val label: String,
    val icon: Painter,
    val action: () -> Unit
)
