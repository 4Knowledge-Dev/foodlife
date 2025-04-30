package com.forknowledge.core.domain.di

import com.forknowledge.core.domain.component.AuthenticationManager
import com.forknowledge.core.domain.component.AuthenticationManagerImpl
import com.forknowledge.core.domain.component.CredentialsManager
import com.forknowledge.core.domain.component.CredentialsManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationModule {

    @Binds
    @Singleton
    abstract fun bindCredentialsManager(credentialsManagerImpl: CredentialsManagerImpl): CredentialsManager

    @Binds
    @Singleton
    abstract fun bindAuthenticationManager(authenticationManagerImpl: AuthenticationManagerImpl): AuthenticationManager
}
