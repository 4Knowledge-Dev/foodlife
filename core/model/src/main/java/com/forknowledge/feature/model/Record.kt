package com.forknowledge.feature.model

import java.util.Date

data class Record(
    val date: Date? = null,
    val intakeCalories: Long = 0,
    val intakeCarbs: Long = 0,
    val intakeProteins: Long = 0,
    val intakeFats: Long = 0,
)
