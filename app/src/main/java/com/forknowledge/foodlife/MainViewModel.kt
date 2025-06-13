package com.forknowledge.foodlife

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forknowledge.core.data.UserRepository
import com.forknowledge.core.data.model.UserAuthState
import com.forknowledge.feature.authentication.AuthenticationRoute
import com.forknowledge.feature.onboarding.OnboardingRoute
import com.forknowledge.feature.planner.PlannerRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _startDestinationRoute = MutableLiveData<Any>(null)
    val startDestinationRoute: LiveData<Any> = _startDestinationRoute

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            userRepository.getUserFlow().collect { state ->
                _startDestinationRoute.value = when (state) {
                    UserAuthState.NONE -> { /* Do nothing */
                    }

                    UserAuthState.UNAUTHENTICATED -> {
                        _isLoading.value = false
                        AuthenticationRoute
                    }

                    UserAuthState.NEW_USER -> {
                        _isLoading.value = false
                        OnboardingRoute
                    }

                    UserAuthState.OLD_USER -> {
                        _isLoading.value = false
                        PlannerRoute
                    }
                }
            }
        }
    }
}