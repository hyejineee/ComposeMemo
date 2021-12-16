package com.example.composememoapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Qualifier

@InstallIn(SingletonComponent::class)
@Module
object SchedulerModule {
    @IOScheduler
    @Provides
    fun provideIOScheduler() = Schedulers.io()

    @AndroidMainScheduler
    @Provides
    fun provideAndroidMainScheduler() = AndroidSchedulers.mainThread()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AndroidMainScheduler

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IOScheduler
