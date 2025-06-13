package com.forknowledge.feature.planner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.data.model.MealPlanDisplayData
import com.forknowledge.core.domain.di.GetMealPlanInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    private val getMealPlanInteractor: GetMealPlanInteractor
): ViewModel() {

    private val _mealPlan = MutableStateFlow<List<MealPlanDisplayData>>(emptyList())
    val mealPlan: StateFlow<List<MealPlanDisplayData>> = _mealPlan

    init {
        viewModelScope.launch {
            getMealPlanInteractor().collect { mealPlan ->
                _mealPlan.update { mealPlan }
            }
        }
    }
}