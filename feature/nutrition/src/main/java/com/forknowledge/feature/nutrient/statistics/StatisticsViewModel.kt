package com.forknowledge.feature.nutrient.statistics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.getLastThirtyDays
import com.forknowledge.core.data.UserRepository
import com.forknowledge.core.data.model.StatisticsDisplayData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _statisticsData = MutableStateFlow<StatisticsDisplayData?>(null)
    val statisticsData: StateFlow<StatisticsDisplayData?> = _statisticsData

    var shouldShowLoading by mutableStateOf(false)
        private set

    var shouldShowError by mutableStateOf(false)
        private set

    init {
        val startDate = getLastThirtyDays().first
        val endDate = getLastThirtyDays().second
        viewModelScope.launch {
            shouldShowLoading = true
            when (val result = userRepository.getNutritionRecordsInAMonth(
                startDate = startDate,
                endDate = endDate
            )) {
                is Result.Loading -> Unit
                is Result.Success -> {
                    shouldShowLoading = false
                    _statisticsData.update { result.data }
                }

                is Result.Error -> {
                    shouldShowLoading = false
                    shouldShowError = true
                }
            }
        }
    }
}
