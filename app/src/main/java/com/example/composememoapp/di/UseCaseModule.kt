package com.example.composememoapp.di

import com.example.composememoapp.data.repository.MemoAppRepository
import com.example.composememoapp.domain.DeleteMemoUseCase
import com.example.composememoapp.domain.GetAllMemoUseCase
import com.example.composememoapp.domain.SaveMemoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object UseCaseModule {
    @Provides
    fun provideGetAllMemoUseCase(appRepository: MemoAppRepository) = GetAllMemoUseCase(appRepository)

    @Provides
    fun provideSaveMemoUseCase(appRepository: MemoAppRepository) = SaveMemoUseCase(appRepository)

    @Provides
    fun provideDeleteMemoUseCase(appRepository: MemoAppRepository) = DeleteMemoUseCase(appRepository)
}
