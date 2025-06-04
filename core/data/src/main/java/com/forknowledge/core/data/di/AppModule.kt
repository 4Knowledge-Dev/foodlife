package com.forknowledge.core.data.di

import com.forknowledge.core.data.UserRepository
import com.forknowledge.core.data.UserRepositoryImpl
import com.forknowledge.core.data.util.ConnectivityManagerNetworkManager
import com.forknowledge.core.data.util.NetworkManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Singleton
    @Binds
    abstract fun bindNetworkMonitor(
        networkManager: ConnectivityManagerNetworkManager,
    ): NetworkManager

    @Singleton
    @Binds
    abstract fun bindUserRepository(
        userRepository: UserRepositoryImpl,
    ): UserRepository
}
