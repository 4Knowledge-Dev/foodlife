package com.forknowledge.core.common.healthtype

enum class TargetWeightError(val error: String) {
    LOWER("Target weight must be greater than your current weight"),
    GREATER("Target weight must be lower than your current weight")
}