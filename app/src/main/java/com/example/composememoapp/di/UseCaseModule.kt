package com.example.composememoapp.di

import com.example.composememoapp.data.repository.MemoRepository
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
    fun provideGetAllMemoUseCase(repository: MemoRepository) = GetAllMemoUseCase(repository)

    @Provides
    fun provideSaveMemoUseCase(repository: MemoRepository) = SaveMemoUseCase(repository)

    @Provides
    fun provideDeleteMemoUseCase(repository: MemoRepository) = DeleteMemoUseCase(repository)
}
