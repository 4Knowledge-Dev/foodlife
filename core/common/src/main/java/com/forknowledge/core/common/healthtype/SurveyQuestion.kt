package com.forknowledge.core.common.healthtype

enum class SurveyQuestionType(
    val question: String,
    val description: String
) {
    GENDER(
        "Your gender?",
        "Since the formula for an accurate calorie calculation differs based on gender, we need to know your gender for calculating your daily calorie goal."
    ),
    BIRTHDAY(
        "What is your birthday?",
        "Your age will affect to daily calorie goal."
    ),
    HEIGHT(
        "How tall are you?",
        "Calorie consumption at rest will also vary by height."
    ),
    CURRENT_WEIGHT(
        "How much do you weight?",
        "Calorie consumption at rest depends on your weight."
    ),
    GOAL(
        "What is your goal?",
        "If don't aim to a specific purpose, why don't you build a healthy lifestyle?"
    ),
    TARGET_WEIGHT("Your target weight", ""),
    ACTIVITY_LEVEL(
        "How active you are?",
        "Your daily activity level will help us calculate your calories needs more accurately."
    ),
    DIET(
        "Which diet do you want to follow?",
        "Your daily nutrition goals and meal plan are based on your diet."
    ),
    EXCLUDE(
        "Allergies",
        "Allergens or ingredients that must be excluded."
    )
}

val questions = SurveyQuestionType.entries.toList()