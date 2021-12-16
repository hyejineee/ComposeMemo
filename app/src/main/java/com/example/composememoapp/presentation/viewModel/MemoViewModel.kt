package com.example.composememoapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.di.AndroidMainScheduler
import com.example.composememoapp.di.IOScheduler
import com.example.composememoapp.domain.GetAllMemoUseCase
import com.example.composememoapp.domain.SaveMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val saveMemoUseCase: SaveMemoUseCase,
    private val getAllMemoUseCase: GetAllMemoUseCase,
    @AndroidMainScheduler private val androidSchedulers: Scheduler,
    @IOScheduler private val ioScheduler: Scheduler,
) : ViewModel() {

    private val statePublishSubject: PublishSubject<MemoState> = PublishSubject.create()
    lateinit var memoList: Flowable<List<MemoEntity>>

    val state = statePublishSubject.publish().autoConnect()

    fun saveMemo(memoEntity: MemoEntity) {
        saveMemoUseCase(memoEntity)
            .subscribeOn(ioScheduler)
            .observeOn(androidSchedulers)
            .subscribe(
                { handleSuccess(MemoState.SaveSuccess) },
                { error -> handleError(error.message) }
            )
    }

    private fun handleSuccess(state: MemoState) {
        Log.d("MemoViewModel", "handleSuccess : $state ")
        statePublishSubject.onNext(state)
    }

    private fun handleError(errorMsg: String?) {
        Log.d("MemoViewModel", "handleError : $errorMsg ")
        statePublishSubject.onNext(MemoState.Error(errorMsg ?: "에러가 발생했습니다."))
    }

    fun getMemo(memoId: Long) = memoList.map { list -> list.find { it.id == memoId } }

    fun getAllMemo() {
        memoList = getAllMemoUseCase()
            .subscribeOn(ioScheduler)
            .observeOn(androidSchedulers)
    }
}
