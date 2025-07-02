package com.forknowledge.feature.nutrient.statistics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.asFlowResult
import com.forknowledge.core.common.getLastThirtyDays
import com.forknowledge.core.data.model.StatisticsDisplayData
import com.forknowledge.core.domain.di.GetNutritionStatisticsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getNutritionStatisticsInteractor: GetNutritionStatisticsInteractor,
) : ViewModel() {

    private val _statisticsData = MutableStateFlow(StatisticsDisplayData())
    val statisticsData: StateFlow<StatisticsDisplayData> = _statisticsData

    var shouldShowLoading by mutableStateOf(false)
        private set

    var shouldShowError by mutableStateOf(false)
        private set

    fun getStatisticsData(nutritionName: String) {
        val startDate = getLastThirtyDays().first
        val endDate = getLastThirtyDays().second
        viewModelScope.launch {
            getNutritionStatisticsInteractor.invoke(
                startDate = startDate,
                endDate = endDate,
                nutritionName = nutritionName
            )
                .asFlowResult()
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            shouldShowLoading = true
                        }

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
}
