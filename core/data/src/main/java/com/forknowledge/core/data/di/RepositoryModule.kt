package com.forknowledge.core.data.di

import com.forknowledge.core.data.AuthenticationManager
import com.forknowledge.core.data.AuthenticationManagerImpl
import com.forknowledge.core.data.CredentialsManager
import com.forknowledge.core.data.CredentialsManagerImpl
import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.data.FoodRepositoryImpl
import com.forknowledge.core.data.UserRepository
import com.forknowledge.core.data.UserRepositoryImpl
import com.forknowledge.core.data.ConnectivityManagerNetworkManager
import com.forknowledge.core.data.NetworkManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindNetworkMonitor(
        networkManager: ConnectivityManagerNetworkManager,
    ): NetworkManager

    @Binds
    @Singleton
    abstract fun bindCredentialsManager(credentialsManagerImpl: CredentialsManagerImpl): CredentialsManager

    @Binds
    @Singleton
    abstract fun bindAuthenticationManager(authenticationManagerImpl: AuthenticationManagerImpl): AuthenticationManager

    @Singleton
    @Binds
    abstract fun bindUserRepository(
        userRepository: UserRepositoryImpl,
    ): UserRepository

    @Singleton
    @Binds
    abstract fun bindFoodRepository(foodRepository: FoodRepositoryImpl): FoodRepository
}
