package com.forknowledge.feature.onboarding.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.forknowledge.core.common.healthtype.SurveyQuestionType
import com.forknowledge.core.common.healthtype.questions
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.Grey7F000000
import com.forknowledge.core.ui.theme.GreyB7BDC4
import com.forknowledge.core.ui.theme.GreyEBEBEB
import com.forknowledge.core.ui.theme.Typography
import com.forknowledge.core.ui.theme.WhiteF9F9F9
import com.forknowledge.core.ui.theme.component.AppButton
import com.forknowledge.core.ui.theme.component.AppText
import com.forknowledge.feature.onboarding.OnboardingViewModel
import com.forknowledge.feature.onboarding.R
import com.forknowledge.feature.onboarding.ui.surveyscreen.ActivityLevelSection
import com.forknowledge.feature.onboarding.ui.surveyscreen.BirthdaySection
import com.forknowledge.feature.onboarding.ui.surveyscreen.DietSection
import com.forknowledge.feature.onboarding.ui.surveyscreen.ExcludeSection
import com.forknowledge.feature.onboarding.ui.surveyscreen.GenderSection
import com.forknowledge.feature.onboarding.ui.surveyscreen.GoalSection
import com.forknowledge.feature.onboarding.ui.surveyscreen.HeightSection
import com.forknowledge.feature.onboarding.ui.surveyscreen.WeightSection

@Composable
fun SurveyScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateToPlanner: () -> Unit
) {
    val question = viewModel.question
    val progress = (viewModel.progress + 1).toFloat() / (questions.size)
    val onNavigateToPlanner = viewModel.onNavigateToPlanner

    if (onNavigateToPlanner) {
        onNavigateToPlanner()
    }

    Scaffold(
        modifier = Modifier
            .background(WhiteF9F9F9)
            .statusBarsPadding(),
        topBar = {
            SurveyTopBar(progress = progress)
        },
        bottomBar = {
            SurveyBottomBar(
                isPreviousVisible = question.ordinal > 0,
                isNextEnabled = viewModel.isNextEnable,
                onPreviousClicked = viewModel::onPreviousClicked,
                onNextClicked = viewModel::onNextClicked
            )
        }
    ) { padding ->
        AnimatedContent(
            modifier = Modifier
                .background(WhiteF9F9F9)
                .padding(padding)
                .padding(horizontal = 16.dp),
            targetState = question,
            transitionSpec = {
                // Compare the incoming number with the previous number.
                if (targetState > initialState) {
                    // If the target number is larger, it slides up and fades in
                    // while the initial (smaller) number slides up and fades out.
                    slideInHorizontally { width -> width } + fadeIn(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = LinearOutSlowInEasing
                        )
                    ) togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut(
                    )
                } else {
                    // If the target number is smaller, it slides down and fades in
                    // while the initial number slides down and fades out.
                    slideInHorizontally { width -> -width } + fadeIn(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = LinearOutSlowInEasing
                        )
                    ) togetherWith
                            slideOutHorizontally { height -> height } + fadeOut()
                }.using(
                    // Disable clipping since the faded slide-in/out should
                    // be displayed out of bounds.
                    SizeTransform(clip = false)
                )
            }
        ) { progress ->

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                AppText(
                    modifier = Modifier.padding(top = 16.dp),
                    text = progress.question,
                    textStyle = Typography.headlineSmall,
                )

                AppText(
                    modifier = Modifier.padding(top = 8.dp),
                    text = progress.description,
                    textStyle = Typography.bodyMedium,
                    color = Grey7F000000
                )

                when (progress) {
                    SurveyQuestionType.GENDER -> GenderSection(
                        gender = viewModel.gender,
                        onGenderSelected = { viewModel.onGenderSelected(it) }
                    )

                    SurveyQuestionType.BIRTHDAY -> BirthdaySection(
                        modifier = Modifier.padding(top = 50.dp),
                        selectedDate = viewModel.birthday,
                        onDateChanged = { viewModel.onBirthdayChanged(it) }
                    )

                    SurveyQuestionType.HEIGHT -> HeightSection(
                        modifier = Modifier.padding(top = 30.dp),
                        isCmUnit = viewModel.isCmUnit,
                        height = viewModel.height,
                        onUnitChanged = { viewModel.onHeightUnitChanged(it) },
                        onHeightChanged = { viewModel.onHeightChanged(it) }
                    )

                    SurveyQuestionType.GOAL -> GoalSection(
                        goal = viewModel.goal,
                        onGoalSelected = { viewModel.onGoalSelected(it) }
                    )

                    SurveyQuestionType.CURRENT_WEIGHT -> WeightSection(
                        isKgUnit = viewModel.isKgUnit,
                        weight = viewModel.currentWeight,
                        onUnitChanged = { viewModel.onWeightUnitChanged(it) },
                        onWeightChanged = { viewModel.onCurrentWeightChanged(it) }
                    )

                    SurveyQuestionType.TARGET_WEIGHT -> WeightSection(
                        isKgUnit = viewModel.isKgUnit,
                        targetWeightError = viewModel.targetWeightError,
                        weight = viewModel.targetWeight,
                        onUnitChanged = { viewModel.onWeightUnitChanged(it) },
                        onWeightChanged = { viewModel.onTargetWeightChanged(it) }
                    )

                    SurveyQuestionType.ACTIVITY_LEVEL -> ActivityLevelSection(
                        level = viewModel.activityLevel,
                        onActivityLevelSelected = { viewModel.onActivityLevelSelected(it) }
                    )

                    SurveyQuestionType.DIET -> DietSection(
                        diet = viewModel.diet,
                        onDietSelected = { viewModel.onDietSelected(it) }
                    )

                    SurveyQuestionType.EXCLUDE -> ExcludeSection(
                        excludes = viewModel.excludes,
                        onIngredientSelected = { viewModel.onIngredientSelected(it) }
                    )

                    else -> {}
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyTopBar(progress: Float) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(23.dp)
                .padding(top = 16.dp),
            progress = { progress },
            color = Green91C747,
            trackColor = GreyB7BDC4,
            gapSize = (-5).dp
        )
    }
}

@Composable
fun SurveyBottomBar(
    isPreviousVisible: Boolean,
    isNextEnabled: Boolean,
    onPreviousClicked: () -> Unit,
    onNextClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isPreviousVisible) {
            AppButton(
                modifier = Modifier.height(50.dp),
                buttonColor = Color.White,
                borderStroke = BorderStroke(1.dp, GreyEBEBEB),
                buttonText = stringResource(R.string.onboarding_survey_previous),
                textColor = Color.Black,
                textStyle = Typography.labelSmall,
                icon = drawable.ic_arrow_previous,
                iconTint = Color.Black,
                isNextButton = false,
                onClicked = onPreviousClicked
            )
        }

        AppButton(
            modifier = Modifier
                .height(50.dp)
                .then(
                    if (!isPreviousVisible) {
                        Modifier.fillMaxWidth()
                    } else {
                        Modifier
                    }
                ),
            buttonColor = Green91C747,
            buttonText = stringResource(R.string.onboarding_survey_next),
            textStyle = Typography.labelSmall,
            icon = drawable.ic_arrow_next,
            enabled = isNextEnabled,
            onClicked = onNextClicked
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SurveyScreenPreview() {
    SurveyScreen()
}
