package com.forknowledge.feature.planner.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.ResultState
import com.forknowledge.core.data.UserRepository
import com.forknowledge.core.data.model.MealPlanDisplayData
import com.forknowledge.core.data.model.TargetNutritionDisplayData
import com.forknowledge.core.domain.di.ClearMealPlanInteractor
import com.forknowledge.core.domain.di.DeleteFromMealPlanInteractor
import com.forknowledge.core.domain.di.GenerateMealPlanInteractor
import com.forknowledge.core.domain.di.GetMealPlanInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

const val NUTRIENTS_CALORIES_INDEX = 0
const val NUTRIENTS_CARB_INDEX = 1
const val NUTRIENTS_PROTEIN_INDEX = 2
const val NUTRIENTS_FAT_INDEX = 3
const val MEAL_SLOT_BREAKFAST = 1
const val MEAL_SLOT_LUNCH = 2
const val MEAL_SLOT_DINNER = 3

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    private val generateMealPlanInteractor: GenerateMealPlanInteractor,
    private val getMealPlanInteractor: GetMealPlanInteractor,
    private val deleteFromMealPlanInteractor: DeleteFromMealPlanInteractor,
    private val clearMealPlanInteractor: ClearMealPlanInteractor,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _targetNutrition = MutableStateFlow<TargetNutritionDisplayData?>(null)
    val targetNutrition: StateFlow<TargetNutritionDisplayData?> = _targetNutrition.asStateFlow()

    private val _mealPlan = MutableStateFlow<List<MealPlanDisplayData>>(emptyList())
    val mealPlan: StateFlow<List<MealPlanDisplayData>> = _mealPlan.asStateFlow()

    var shouldShowLoading by mutableStateOf(false)
        private set

    var shouldShowError by mutableStateOf(false)
        private set

    var deleteRecipeState by mutableStateOf(ResultState.NONE)
        private set

    var onProcessItem by mutableIntStateOf(0)
        private set

    init {
        getUserTargetNutrition()
        getMealPlan()
    }

    private fun getUserTargetNutrition() {
        viewModelScope.launch {
            userRepository.getUserTargetNutrition().collect { nutrition ->
                if (nutrition != null) {
                    _targetNutrition.update { nutrition }
                }
            }
        }
    }

    private fun updateMealPlanState(
        date: LocalDate,
        meal: Int,
        mealId: Int
    ) {
        _mealPlan.update { meals ->
            when (meal) {
                MEAL_SLOT_BREAKFAST -> {
                    meals.map { mealDay ->
                        if (mealDay.date == date) {
                            val deleteMeal =
                                mealDay.breakfast.first { it.mealId == mealId } // Meal that being deleted
                            val remainingMeals =
                                mealDay.breakfast.filterNot { it.mealId == mealId } // Remain meals after removing deleteMeal
                            val newNutritionSummary = if (deleteMeal.calories == null) {
                                listOf(
                                    mealDay.nutritionSummary[NUTRIENTS_CALORIES_INDEX] - (mealDay.breakfastNutrition[NUTRIENTS_CALORIES_INDEX] - remainingMeals.sumOf { it.calories!! }),
                                    mealDay.nutritionSummary[NUTRIENTS_CARB_INDEX] - (mealDay.breakfastNutrition[NUTRIENTS_CARB_INDEX] - remainingMeals.sumOf { it.carbs!! }),
                                    mealDay.nutritionSummary[NUTRIENTS_PROTEIN_INDEX] - (mealDay.breakfastNutrition[NUTRIENTS_PROTEIN_INDEX] - remainingMeals.sumOf { it.protein!! }),
                                    mealDay.nutritionSummary[NUTRIENTS_FAT_INDEX] - (mealDay.breakfastNutrition[NUTRIENTS_FAT_INDEX] - remainingMeals.sumOf { it.fat!! }),
                                )
                            } else {
                                listOf(
                                    mealDay.nutritionSummary[NUTRIENTS_CALORIES_INDEX] - deleteMeal.calories!!,
                                    mealDay.nutritionSummary[NUTRIENTS_CARB_INDEX] - deleteMeal.carbs!!,
                                    mealDay.nutritionSummary[NUTRIENTS_PROTEIN_INDEX] - deleteMeal.protein!!,
                                    mealDay.nutritionSummary[NUTRIENTS_FAT_INDEX] - deleteMeal.fat!!
                                )
                            }
                            val newBreakfastNutrition = if (deleteMeal.calories == null) {
                                listOf(
                                    mealDay.breakfastNutrition[NUTRIENTS_CALORIES_INDEX] - (mealDay.breakfastNutrition[NUTRIENTS_CALORIES_INDEX] - remainingMeals.sumOf { it.calories!! }),
                                    mealDay.breakfastNutrition[NUTRIENTS_CARB_INDEX] - (mealDay.breakfastNutrition[NUTRIENTS_CARB_INDEX] - remainingMeals.sumOf { it.carbs!! }),
                                    mealDay.breakfastNutrition[NUTRIENTS_PROTEIN_INDEX] - (mealDay.breakfastNutrition[NUTRIENTS_PROTEIN_INDEX] - remainingMeals.sumOf { it.protein!! }),
                                    mealDay.breakfastNutrition[NUTRIENTS_FAT_INDEX] - (mealDay.breakfastNutrition[NUTRIENTS_FAT_INDEX] - remainingMeals.sumOf { it.fat!! }),
                                )
                            } else {
                                listOf(
                                    mealDay.breakfastNutrition[NUTRIENTS_CALORIES_INDEX] - deleteMeal.calories!!,
                                    mealDay.breakfastNutrition[NUTRIENTS_CARB_INDEX] - deleteMeal.carbs!!,
                                    mealDay.breakfastNutrition[NUTRIENTS_PROTEIN_INDEX] - deleteMeal.protein!!,
                                    mealDay.breakfastNutrition[NUTRIENTS_FAT_INDEX] - deleteMeal.fat!!
                                )
                            }

                            mealDay.copy(
                                nutritionSummary = newNutritionSummary,
                                breakfastNutrition = newBreakfastNutrition,
                                breakfast = remainingMeals
                            )
                        } else {
                            mealDay
                        }
                    }
                }

                MEAL_SLOT_LUNCH -> {
                    meals.map { mealDay ->
                        if (mealDay.date == date) {
                            val deleteMeal =
                                mealDay.lunch.first { it.mealId == mealId } // Meal that being deleted
                            val remainingMeals =
                                mealDay.lunch.filterNot { it.mealId == mealId } // Remain meals after removing deleteMeal
                            val newNutritionSummary = if (deleteMeal.calories == null) {
                                listOf(
                                    mealDay.nutritionSummary[NUTRIENTS_CALORIES_INDEX] - (mealDay.lunchNutrition[NUTRIENTS_CALORIES_INDEX] - remainingMeals.sumOf { it.calories!! }),
                                    mealDay.nutritionSummary[NUTRIENTS_CARB_INDEX] - (mealDay.lunchNutrition[NUTRIENTS_CARB_INDEX] - remainingMeals.sumOf { it.carbs!! }),
                                    mealDay.nutritionSummary[NUTRIENTS_PROTEIN_INDEX] - (mealDay.lunchNutrition[NUTRIENTS_PROTEIN_INDEX] - remainingMeals.sumOf { it.protein!! }),
                                    mealDay.nutritionSummary[NUTRIENTS_FAT_INDEX] - (mealDay.lunchNutrition[NUTRIENTS_FAT_INDEX] - remainingMeals.sumOf { it.fat!! }),
                                )
                            } else {
                                listOf(
                                    mealDay.nutritionSummary[NUTRIENTS_CALORIES_INDEX] - deleteMeal.calories!!,
                                    mealDay.nutritionSummary[NUTRIENTS_CARB_INDEX] - deleteMeal.carbs!!,
                                    mealDay.nutritionSummary[NUTRIENTS_PROTEIN_INDEX] - deleteMeal.protein!!,
                                    mealDay.nutritionSummary[NUTRIENTS_FAT_INDEX] - deleteMeal.fat!!
                                )
                            }
                            val newLunchNutrition = if (deleteMeal.calories == null) {
                                listOf(
                                    mealDay.lunchNutrition[NUTRIENTS_CALORIES_INDEX] - (mealDay.lunchNutrition[NUTRIENTS_CALORIES_INDEX] - remainingMeals.sumOf { it.calories!! }),
                                    mealDay.lunchNutrition[NUTRIENTS_CARB_INDEX] - (mealDay.lunchNutrition[NUTRIENTS_CARB_INDEX] - remainingMeals.sumOf { it.carbs!! }),
                                    mealDay.lunchNutrition[NUTRIENTS_PROTEIN_INDEX] - (mealDay.lunchNutrition[NUTRIENTS_PROTEIN_INDEX] - remainingMeals.sumOf { it.protein!! }),
                                    mealDay.lunchNutrition[NUTRIENTS_FAT_INDEX] - (mealDay.lunchNutrition[NUTRIENTS_FAT_INDEX] - remainingMeals.sumOf { it.fat!! }),
                                )
                            } else {
                                listOf(
                                    mealDay.lunchNutrition[NUTRIENTS_CALORIES_INDEX] - deleteMeal.calories!!,
                                    mealDay.lunchNutrition[NUTRIENTS_CARB_INDEX] - deleteMeal.carbs!!,
                                    mealDay.lunchNutrition[NUTRIENTS_PROTEIN_INDEX] - deleteMeal.protein!!,
                                    mealDay.lunchNutrition[NUTRIENTS_FAT_INDEX] - deleteMeal.fat!!
                                )
                            }

                            mealDay.copy(
                                nutritionSummary = newNutritionSummary,
                                lunchNutrition = newLunchNutrition,
                                lunch = remainingMeals
                            )
                        } else {
                            mealDay
                        }
                    }
                }

                MEAL_SLOT_DINNER -> {
                    meals.map { mealDay ->
                        if (mealDay.date == date) {
                            val deleteMeal =
                                mealDay.dinner.first { it.mealId == mealId } // Meal that being deleted
                            val remainingMeals =
                                mealDay.dinner.filterNot { it.mealId == mealId } // Remain meals after removing deleteMeal
                            val newNutritionSummary = if (deleteMeal.calories == null) {
                                listOf(
                                    mealDay.nutritionSummary[NUTRIENTS_CALORIES_INDEX] - (mealDay.dinnerNutrition[NUTRIENTS_CALORIES_INDEX] - remainingMeals.sumOf { it.calories!! }),
                                    mealDay.nutritionSummary[NUTRIENTS_CARB_INDEX] - (mealDay.dinnerNutrition[NUTRIENTS_CARB_INDEX] - remainingMeals.sumOf { it.carbs!! }),
                                    mealDay.nutritionSummary[NUTRIENTS_PROTEIN_INDEX] - (mealDay.dinnerNutrition[NUTRIENTS_PROTEIN_INDEX] - remainingMeals.sumOf { it.protein!! }),
                                    mealDay.nutritionSummary[NUTRIENTS_FAT_INDEX] - (mealDay.dinnerNutrition[NUTRIENTS_FAT_INDEX] - remainingMeals.sumOf { it.fat!! }),
                                )
                            } else {
                                listOf(
                                    mealDay.nutritionSummary[NUTRIENTS_CALORIES_INDEX] - deleteMeal.calories!!,
                                    mealDay.nutritionSummary[NUTRIENTS_CARB_INDEX] - deleteMeal.carbs!!,
                                    mealDay.nutritionSummary[NUTRIENTS_PROTEIN_INDEX] - deleteMeal.protein!!,
                                    mealDay.nutritionSummary[NUTRIENTS_FAT_INDEX] - deleteMeal.fat!!
                                )
                            }
                            val newDinnerNutrition = if (remainingMeals.isNotEmpty()) {
                                listOf(
                                    mealDay.dinnerNutrition[NUTRIENTS_CALORIES_INDEX] - (mealDay.dinnerNutrition[NUTRIENTS_CALORIES_INDEX] - remainingMeals.sumOf { it.calories!! }),
                                    mealDay.dinnerNutrition[NUTRIENTS_CARB_INDEX] - (mealDay.dinnerNutrition[NUTRIENTS_CARB_INDEX] - remainingMeals.sumOf { it.carbs!! }),
                                    mealDay.dinnerNutrition[NUTRIENTS_PROTEIN_INDEX] - (mealDay.dinnerNutrition[NUTRIENTS_PROTEIN_INDEX] - remainingMeals.sumOf { it.protein!! }),
                                    mealDay.dinnerNutrition[NUTRIENTS_FAT_INDEX] - (mealDay.dinnerNutrition[NUTRIENTS_FAT_INDEX] - remainingMeals.sumOf { it.fat!! }),
                                )
                            } else {
                                listOf(
                                    mealDay.dinnerNutrition[NUTRIENTS_CALORIES_INDEX] - deleteMeal.calories!!,
                                    mealDay.dinnerNutrition[NUTRIENTS_CARB_INDEX] - deleteMeal.carbs!!,
                                    mealDay.dinnerNutrition[NUTRIENTS_PROTEIN_INDEX] - deleteMeal.protein!!,
                                    mealDay.dinnerNutrition[NUTRIENTS_FAT_INDEX] - deleteMeal.fat!!
                                )
                            }

                            mealDay.copy(
                                nutritionSummary = newNutritionSummary,
                                dinnerNutrition = newDinnerNutrition,
                                dinner = remainingMeals
                            )
                        } else {
                            mealDay
                        }
                    }
                }

                else -> meals
            }
        }
    }

    fun createMealPlan() {
        shouldShowLoading = true
        viewModelScope.launch {
            when (val result = generateMealPlanInteractor(_mealPlan.value)) {
                is Result.Loading -> { /* Do nothing */
                }

                is Result.Success -> {
                    shouldShowLoading = false
                    _mealPlan.update { result.data }
                }

                is Result.Error -> {
                    shouldShowLoading = false
                }
            }
        }
    }

    fun getMealPlan() {
        shouldShowLoading = true
        shouldShowError = false
        viewModelScope.launch {
            when (val result = getMealPlanInteractor()) {
                is Result.Loading -> { /* Do nothing */
                }

                is Result.Success -> {
                    shouldShowLoading = false
                    _mealPlan.update { result.data }
                }

                is Result.Error -> {
                    shouldShowLoading = false
                    shouldShowError = true
                }
            }
        }
    }

    fun deleteRecipeFromMealPlan(
        date: LocalDate,
        mealPosition: Int,
        mealId: Int
    ) {
        onProcessItem = mealId
        deleteRecipeState = ResultState.NONE
        viewModelScope.launch {
            when (deleteFromMealPlanInteractor(mealId)) {
                is Result.Loading -> { /* Do nothing */
                }

                is Result.Success -> {
                    deleteRecipeState = ResultState.SUCCESS
                    updateMealPlanState(
                        date = date,
                        meal = mealPosition,
                        mealId = mealId
                    )
                    onProcessItem = 0
                }

                is Result.Error -> {
                    onProcessItem = 0
                    deleteRecipeState = ResultState.FAILURE
                }
            }
        }
    }

    fun clearMealPlan() {
        if (_mealPlan.value.isNotEmpty()) {
            shouldShowLoading = true
            viewModelScope.launch {
                when (clearMealPlanInteractor(_mealPlan.value)) {
                    is Result.Loading -> { /* Do nothing */
                    }

                    is Result.Success -> {
                        _mealPlan.update { emptyList() }
                        shouldShowLoading = false
                    }

                    is Result.Error -> {
                        shouldShowLoading = false
                    }
                }
            }
        }
    }
}
