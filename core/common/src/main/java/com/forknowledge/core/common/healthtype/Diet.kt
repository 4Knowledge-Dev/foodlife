package com.forknowledge.core.common.healthtype

enum class Diet(
    val dietName: String,
    val description: String,
    val focus: String,
    val goal: String,
    val typeUrl: String,
    val macro: Macros
) {
    BALANCE(
        dietName = "Balance",
        description = "Supports overall health",
        focus = "Consuming a wide variety of foods from all major food groups in balanced ratio of " +
                "of carbs, protein, and fat.",
        goal = "Provides the body with all essential nutrients (carbohydrates, proteins, fats, " +
                "vitamins, minerals, water, fiber) needed for optimal health, energy, growth, repair," +
                " and disease prevention. It emphasizes moderation and variety rather than strict " +
                "limitation of any single group.",
        typeUrl = "",
        macro = Macros(
            carbs = 0.55f,
            protein = 0.2f,
            fat = 0.25f
        )
    ),

    LOW_CARB(
        dietName = "Low Carb",
        description = "Helps weight loss",
        focus = "Significantly reducing the intake of carbohydrates, especially refined carbs and " +
                "sugars (found in bread, pasta, rice, sugary drinks, sweets). Intake of protein and" +
                " fat is typically increased to compensate.",
        goal = "Primarily used for long-term weight loss, blood sugar management and metabolic heath.",
        typeUrl = "ketogenic",
        macro = Macros(
            carbs = 0.3f,
            protein = 0.25f,
            fat = 0.45f
        )
    ),

    HIGH_PROTEIN(
        dietName = "High Protein",
        description = "Supports muscle strength",
        focus = "Increasing the proportion of total daily calories that come from protein sources " +
                "(like meat, poultry, fish, eggs, dairy, legumes, tofu). This often means consuming " +
                "more protein than standard dietary guidelines recommend.",
        goal = "Often pursued for weight management (protein increases satiety/fullness), muscle " +
                "building and repair (especially for athletes or those strength training), or " +
                "preserving muscle mass during aging or weight loss.",
        typeUrl = "",
        macro = Macros(
            carbs = 0.35f,
            protein = 0.35f,
            fat = 0.3f
        )
    ),

    KETO(
        dietName = "Keto",
        description = "Uses most of fat for energy",
        focus = "The keto diet is based more on the ratio of fat, protein, and carbs in the diet rather than " +
                "specific ingredients. Generally speaking, high fat, protein-rich foods are acceptable " +
                "and high carbohydrate foods are not.",
        goal = "Very low-carb diets like Keto restrict carbs drastically to induce fat burning.",
        typeUrl = "ketogenic",
        macro = Macros(
            carbs = 0.1f,
            protein = 0.25f,
            fat = 0.65f
        )
    ),

    VEGAN(
        dietName = "Vegan",
        description = "Aim to vegetarian",
        focus = "No ingredients may contain meat or meat by-products, such as bones or gelatin, nor " +
                "may they contain eggs, dairy, or honey.",
        goal = "",
        typeUrl = "vegan",
        macro = Macros(
            carbs = 0.5f,
            protein = 0.2f,
            fat = 0.3f
        )
    ),
}

data class Macros(
    val protein: Float,
    val carbs: Float,
    val fat: Float
)
