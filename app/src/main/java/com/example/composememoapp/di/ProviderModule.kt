package com.example.composememoapp.di

import android.content.Context
import com.example.composememoapp.util.BitmapProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object ProviderModule {

    @Provides
    fun provideImageProvider(@ApplicationContext context: Context) = BitmapProvider(context = context)
}
