package com.forknowledge.feature.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.common.Result
import com.forknowledge.core.common.calculateTargetCalories
import com.forknowledge.core.common.extension.toAge
import com.forknowledge.core.common.healthtype.ActivityLevel
import com.forknowledge.core.common.healthtype.Diet
import com.forknowledge.core.common.healthtype.Gender
import com.forknowledge.core.common.healthtype.Goal
import com.forknowledge.core.common.healthtype.SurveyQuestionType
import com.forknowledge.core.common.healthtype.TargetWeightError
import com.forknowledge.core.common.healthtype.questions
import com.forknowledge.core.data.UserRepository
import com.forknowledge.feature.model.userdata.TargetNutrition
import com.forknowledge.feature.model.userdata.User
import com.forknowledge.feature.onboarding.ui.DataUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var onNavigateToPlanner by mutableStateOf(false)
        private set

    var progress by mutableIntStateOf(0)
        private set
    val question
        get() = questions[progress].also {
            isNextEnable = when (it) {
                SurveyQuestionType.GENDER -> gender != null
                SurveyQuestionType.BIRTHDAY -> birthday != null
                SurveyQuestionType.HEIGHT -> true
                SurveyQuestionType.GOAL -> goal != null
                SurveyQuestionType.CURRENT_WEIGHT -> true
                SurveyQuestionType.TARGET_WEIGHT -> targetWeightError == null
                SurveyQuestionType.ACTIVITY_LEVEL -> activityLevel != null
                SurveyQuestionType.DIET -> diet != null
                SurveyQuestionType.EXCLUDE -> true
            }
        }

    var gender by mutableStateOf<Gender?>(null)
        private set

    var birthday by mutableStateOf<Long?>(null)
        private set

    var height by mutableDoubleStateOf(DataUnit.DEFAULT_HEIGHT_IN_FEET)
        private set

    var currentWeight by mutableDoubleStateOf(DataUnit.DEFAULT_WEIGHT_IN_KILOGRAM)
        private set

    var targetWeight by mutableDoubleStateOf(DataUnit.DEFAULT_WEIGHT_IN_KILOGRAM)
        private set

    var targetWeightError by mutableStateOf<TargetWeightError?>(null)

    var goal by mutableStateOf<Goal?>(null)
        private set

    var activityLevel by mutableStateOf<ActivityLevel?>(null)
        private set

    var weightPerWeek by mutableDoubleStateOf(0.5)
        private set

    var diet by mutableStateOf<Diet?>(null)
        private set

    var excludes by mutableStateOf<List<String>>(listOf())
        private set

    var isCmUnit by mutableStateOf(true)
        private set

    var isKgUnit by mutableStateOf(true)
        private set

    var isNextEnable by mutableStateOf(false)
        private set

    var targetCalories = 0

    private fun createUserInfo(): User {
        return User(
            isNewUser = false,
            gender = gender == Gender.MALE,
            birthday = Date(birthday!!),
            height = height,
            goal = goal!!.ordinal.toLong(),
            currentWeight = currentWeight,
            targetWeight = targetWeight,
            weighPerWeek = weightPerWeek,
            isHeightUnitCm = isCmUnit,
            isWeightUnitKg = isKgUnit,
            activityLevel = activityLevel!!.ordinal.toLong(),
            diet = diet!!.ordinal.toLong(),
            excludes = excludes,
            targetNutrition = TargetNutrition(
                calories = targetCalories.toLong(),
                carbRatio = diet!!.macro.carbs,
                proteinRatio = diet!!.macro.protein,
                fatRatio = diet!!.macro.fat,
            )
        )
    }

    fun onPreviousClicked() {
        progress -= if (questions[progress] == SurveyQuestionType.ACTIVITY_LEVEL) {
            when (goal) {
                Goal.EAT_HEALTHY -> 2
                else -> 1
            }
        } else {
            1
        }
    }

    fun onNextClicked() {
        if (questions[progress] == SurveyQuestionType.EXCLUDE) {
            targetCalories = calculateTargetCalories(
                gender = gender == Gender.MALE,
                age = birthday!!.toAge(),
                height = height,
                weight = currentWeight,
                activityIndex = activityLevel!!.index,
                goal = goal!!,
                weightPerWeek = weightPerWeek,
            )
            viewModelScope.launch {
                when (userRepository.updateUserInfo(createUserInfo())) {
                    is Result.Loading -> { /* Do nothing */ }
                    is Result.Success -> {
                        onNavigateToPlanner = true
                    }
                    is Result.Error -> { /* Do nothing */ }
                }
            }
            return
        }
        progress += if (questions[progress] == SurveyQuestionType.GOAL) {
            when (goal) {
                Goal.EAT_HEALTHY -> 2
                else -> 1
            }
        } else {
            1
        }
    }

    fun onGenderSelected(userGender: Gender) {
        gender = userGender
    }

    fun onBirthdayChanged(userBirthday: Long) {
        birthday = userBirthday
    }

    fun onHeightChanged(userHeight: Double) {
        height = userHeight
    }

    fun onCurrentWeightChanged(weight: Double) {
        currentWeight = weight
    }

    fun onTargetWeightChanged(weight: Double) {
        when (goal) {
            Goal.LOSE_WEIGHT -> {
                if (weight >= currentWeight) {
                    targetWeightError = TargetWeightError.GREATER
                } else {
                    targetWeight = weight
                    targetWeightError = null
                }
            }

            Goal.GAIN_WEIGHT, Goal.BUILD_MUSCLE -> {
                if (weight <= currentWeight) {
                    targetWeightError = TargetWeightError.LOWER
                } else {
                    targetWeight = weight
                    targetWeightError = null
                }
            }

            else -> { /* Nothing to do */
            }
        }
    }

    fun onGoalSelected(userGoal: Goal) {
        goal = userGoal
    }

    fun onActivityLevelSelected(userActivityLevel: ActivityLevel) {
        activityLevel = userActivityLevel
    }

    fun onDietSelected(userDiet: Diet) {
        diet = userDiet
    }

    fun onIngredientSelected(exclude: String) {
        excludes = excludes.toMutableList().apply {
            if (contains(exclude)) {
                remove(exclude)
            } else {
                add(exclude)
            }
        }
    }

    fun onHeightUnitChanged(lengthUnit: Boolean) {
        isCmUnit = lengthUnit
    }

    fun onWeightUnitChanged(weightUnit: Boolean) {
        isKgUnit = weightUnit
    }
}
