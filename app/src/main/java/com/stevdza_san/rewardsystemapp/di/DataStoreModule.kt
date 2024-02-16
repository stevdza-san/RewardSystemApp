package com.stevdza_san.rewardsystemapp.di

import android.content.Context
import com.stevdza_san.rewardsystemapp.data.DataStoreOperationsImpl
import com.stevdza_san.rewardsystemapp.domain.DataStoreOperations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDataStoreOperations(
        @ApplicationContext context: Context
    ): DataStoreOperations {
        return DataStoreOperationsImpl(context = context)
    }
}