package com.example.composememoapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.composememoapp.data.database.entity.TagEntity
import com.example.composememoapp.di.AndroidMainScheduler
import com.example.composememoapp.di.IOScheduler
import com.example.composememoapp.domain.GetAllTagUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(
    private val getAllTagUseCase: GetAllTagUseCase,
    @AndroidMainScheduler private val androidScheduler: Scheduler,
    @IOScheduler private val ioScheduler: Scheduler
) : ViewModel() {

    private val _tagListSource = BehaviorSubject.create<List<TagEntity>>()

    val tagList: Observable<List<TagEntity>> =
        Observable.zip(
            _tagListSource, Observable.just(""),
            BiFunction { t1: List<TagEntity>, t2: String ->
                t1
            }
        )

    init {
        _tagListSource.subscribe {
            Log.d("TagViewModel", "_taglist : $it")
        }

        tagList.subscribe {
            Log.d("TagViewModel", "tag : $it")
        }
    }

    fun getAllTag() {
        Log.d("TagViewModel", "getAllTag() is called")
        getAllTagUseCase()
            .subscribeOn(ioScheduler)
            .observeOn(androidScheduler)
            .sorted()
            .subscribe {
                _tagListSource.onNext(it)
            }
    }
}
