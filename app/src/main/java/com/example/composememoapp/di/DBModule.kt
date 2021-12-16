package com.example.composememoapp.di

import android.content.Context
import androidx.room.Room
import com.example.composememoapp.data.database.MemoAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DBModule {
    @Provides
    @Singleton
    fun provideMemoAppDatabase(@ApplicationContext context: Context): MemoAppDatabase = Room
        .databaseBuilder(context, MemoAppDatabase::class.java, "memo_app.db")
        .build()

    @Provides
    fun provideMemoDao(appDatabase: MemoAppDatabase) = appDatabase.memoDao()
}
