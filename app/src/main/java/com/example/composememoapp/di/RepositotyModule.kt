package com.example.composememoapp.di

import com.example.composememoapp.data.repository.DefaultMemoRepository
import com.example.composememoapp.data.repository.MemoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindsDefaultMemoRepository(
        defaultMemoRepository: DefaultMemoRepository
    ): MemoRepository
}