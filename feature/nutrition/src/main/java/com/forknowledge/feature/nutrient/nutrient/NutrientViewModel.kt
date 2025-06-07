package com.forknowledge.feature.nutrient.nutrient

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.common.getCurrentDate
import com.forknowledge.core.data.UserRepository
import com.forknowledge.feature.model.IntakeNutrition
import com.forknowledge.feature.model.TargetNutrition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    var date by mutableStateOf(getCurrentDate())
        private set

    private val _targetNutrition = MutableStateFlow<TargetNutrition?>(null)
    val targetNutrition: StateFlow<TargetNutrition?> = _targetNutrition

    private val _intakeNutrition = MutableStateFlow<IntakeNutrition>(IntakeNutrition())
    val intakeNutrition: StateFlow<IntakeNutrition> = _intakeNutrition

    init {
        getUserNutrition()
    }

    private fun getUserNutrition() {
        viewModelScope.launch {
            userRepository.getUserTargetNutrition().collect { nutrition ->
                if (nutrition != null) {
                    _targetNutrition.update { nutrition }
                }
            }
        }
    }

    fun getIntakeNutritionByDate() {
        viewModelScope.launch {
            userRepository.getUserNutritionRecordByDate(date).collect { record ->
                _intakeNutrition.update { record }
            }
        }
    }

    fun updateDate(newDate: Date) {
        date = newDate
    }
}
