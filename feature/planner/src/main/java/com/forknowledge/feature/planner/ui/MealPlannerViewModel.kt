package com.forknowledge.feature.planner.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.ResultState
import com.forknowledge.core.common.extension.toYearMonthDateString
import com.forknowledge.core.data.model.MealPlanDisplayData
import com.forknowledge.core.domain.di.DeleteFromMealPlanInteractor
import com.forknowledge.core.domain.di.GetMealPlanInteractor
import com.forknowledge.feature.planner.getFirstDayOfWeek
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    private val getMealPlanInteractor: GetMealPlanInteractor,
    private val deleteFromMealPlanInteractor: DeleteFromMealPlanInteractor
) : ViewModel() {

    private val _mealPlan = MutableStateFlow<List<MealPlanDisplayData>>(emptyList())
    val mealPlan: StateFlow<List<MealPlanDisplayData>> = _mealPlan

    var shouldShowLoading by mutableStateOf(false)
        private set

    var shouldShowError by mutableStateOf(false)
        private set

    var deleteRecipeState by mutableStateOf<ResultState?>(null)
        private set

    var onProcessItem by mutableIntStateOf(0)
        private set

    init {
        getMealPlan()
    }

    private fun updateMealPlanState(
        date: LocalDate,
        mealPosition: Int,
        mealId: Int
    ) {
        _mealPlan.update { meals ->
            when (mealPosition) {
                1 -> {
                    meals.map { mealDay ->
                        if (mealDay.date == date) {
                            mealDay.copy(
                                breakfast = mealDay.breakfast.filter { it.mealId != mealId }
                            )
                        } else {
                            mealDay
                        }
                    }
                }

                2 -> {
                    meals.map { mealDay ->
                        if (mealDay.date == date) {
                            mealDay.copy(
                                lunch = mealDay.lunch.filter { it.mealId != mealId }
                            )
                        } else {
                            mealDay
                        }
                    }
                }

                else -> {
                    meals.map { mealDay ->
                        if (mealDay.date == date) {
                            mealDay.copy(
                                dinner = mealDay.dinner.filter { it.mealId != mealId }
                            )
                        } else {
                            mealDay
                        }
                    }
                }
            }
        }
    }

    fun getMealPlan() {
        shouldShowLoading = true
        shouldShowError = false
        viewModelScope.launch {
            when (val result = getMealPlanInteractor(getFirstDayOfWeek().toYearMonthDateString())) {
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
        viewModelScope.launch {
            when (deleteFromMealPlanInteractor(mealId)) {
                is Result.Loading -> { /* Do nothing */
                }

                is Result.Success -> {
                    deleteRecipeState = ResultState.SUCCESS
                    onProcessItem = 0
                    updateMealPlanState(
                        date = date,
                        mealPosition = mealPosition,
                        mealId = mealId
                    )
                }

                is Result.Error -> {
                    onProcessItem = 0
                    deleteRecipeState = ResultState.FAILURE
                }
            }
        }
    }
}
