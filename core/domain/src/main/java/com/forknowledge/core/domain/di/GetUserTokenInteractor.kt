package com.forknowledge.core.domain.di

import com.forknowledge.core.api.model.post.ConnectUser
import com.forknowledge.core.common.Result
import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.data.UserRepository
import com.forknowledge.feature.model.userdata.UserToken
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetUserTokenInteractor @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val foodRepository: FoodRepository
) {

    suspend operator fun <R> invoke(
        block: suspend (UserToken) -> R
    ): Result<R> {
        return try {
            val userToken = getUserToken()
            if (userToken.isValid()) {
                Result.Success(block(userToken))
            } else {
                Result.Error(Exception("Invalid user token."))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private suspend fun getUserToken(): UserToken {
        return when (val result = userRepository.getUserTokenFromLocal()) {
            is Result.Success -> {
                if (result.data.isValid()) {
                    result.data
                } else {
                    when (val result = userRepository.getUserTokenFromServer()) {
                        is Result.Success -> {
                            if (result.data.isValid()) {
                                userRepository.saveUserTokenToLocal(
                                    username = result.data.username,
                                    hashKey = result.data.hashKey
                                )
                                result.data
                            } else {
                                when (val result =
                                    foodRepository.connectUser(ConnectUser(auth.currentUser!!.email!!))) {
                                    is Result.Success -> {
                                        when (userRepository.saveUserTokenToServer(
                                            username = result.data.username,
                                            hashKey = result.data.hashKey
                                        )) {
                                            is Result.Success -> {
                                                userRepository.saveUserTokenToLocal(
                                                    username = result.data.username,
                                                    hashKey = result.data.hashKey
                                                )
                                                result.data
                                            }

                                            else -> {
                                                UserToken("", "")
                                            }
                                        }
                                    }

                                    else -> {
                                        UserToken("", "")
                                    }
                                }

                            }
                        }

                        else -> {
                            UserToken("", "")
                        }
                    }
                }
            }

            else -> {
                UserToken("", "")
            }
        }
    }
}
