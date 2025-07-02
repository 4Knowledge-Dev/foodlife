package com.forknowledge.feature.nutrient.dailyinsights

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.common.Result
import com.forknowledge.core.data.UserRepository
import com.forknowledge.core.data.model.DailyNutritionDisplayData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userNutritionRecord = MutableStateFlow<DailyNutritionDisplayData?>(null)
    val userNutritionRecord: StateFlow<DailyNutritionDisplayData?> = _userNutritionRecord

    var shouldShowLoading by mutableStateOf(false)
        private set

    var shouldShowError by mutableStateOf(false)
        private set

    fun getDailyNutritionRecord(date: Date) {
        shouldShowLoading = true
        viewModelScope.launch {
            when (val result = userRepository.getDailyNutritionInfo(date)) {
                is Result.Loading -> Unit
                is Result.Success -> {
                    shouldShowLoading = false
                    _userNutritionRecord.update { result.data }
                }

                is Result.Error -> {
                    shouldShowLoading = false
                    shouldShowError = true
                }
            }
        }
    }
}
