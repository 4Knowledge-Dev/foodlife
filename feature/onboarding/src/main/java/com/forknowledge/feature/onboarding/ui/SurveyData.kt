package com.forknowledge.feature.onboarding.ui

val heightInCmRange = 18..302
val heightInFeetRange = 1..9
val weightInKgRange = 8..502
val weightInLbRange = 20..1104
val fractionRange = -2..11

class DataUnit {
    companion object {
        const val DEFAULT_HEIGHT_IN_FEET = 5.5
        const val DEFAULT_WEIGHT_IN_KILOGRAM = 65.5
        const val MIN_HEIGHT_IN_CENTIMETER = 20
        const val MAX_HEIGHT_IN_CENTIMETER = 300
        const val MIN_WEIGHT_IN_KILOGRAM = 10
        const val MAX_WEIGHT_IN_KILOGRAM = 500
    }
}
