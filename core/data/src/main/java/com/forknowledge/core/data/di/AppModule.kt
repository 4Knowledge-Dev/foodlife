package com.forknowledge.core.data.di

import com.forknowledge.core.data.util.ConnectivityManagerNetworkManager
import com.forknowledge.core.data.util.NetworkManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindNetworkMonitor(
        networkManager: ConnectivityManagerNetworkManager,
    ): NetworkManager
}