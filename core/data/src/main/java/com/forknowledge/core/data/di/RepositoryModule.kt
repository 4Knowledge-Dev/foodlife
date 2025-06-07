package com.forknowledge.core.data.di

import com.forknowledge.core.data.FoodRepository
import com.forknowledge.core.data.FoodRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindFoodRepository(foodRepository: FoodRepositoryImpl): FoodRepository

}