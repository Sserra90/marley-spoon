package com.marleyspoon.base_android

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class CoroutinesModule {
    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO
}