package com.forknowledge.feature.planner.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.asFlowResult
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
) : ViewModel() {

    private val _mealPlan = MutableStateFlow<List<MealPlanDisplayData>>(emptyList())
    val mealPlan: StateFlow<List<MealPlanDisplayData>> = _mealPlan

    var shouldShowLoading by mutableStateOf(false)
        private set

    var shouldShowError by mutableStateOf(false)
        private set

    init {
        getMealPlan()
    }

    fun getMealPlan() {
        shouldShowError = false
        viewModelScope.launch {
            getMealPlanInteractor.invoke()
                .asFlowResult()
                .collect { result ->
                    when (result) {
                        is Result.Loading -> shouldShowLoading = true
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
    }
}
