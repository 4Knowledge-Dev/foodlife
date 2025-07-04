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
