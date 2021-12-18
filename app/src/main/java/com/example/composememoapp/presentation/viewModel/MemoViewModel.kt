package com.example.composememoapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.composememoapp.data.ContentBlock
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.di.AndroidMainScheduler
import com.example.composememoapp.di.IOScheduler
import com.example.composememoapp.domain.DeleteMemoUseCase
import com.example.composememoapp.domain.GetAllMemoUseCase
import com.example.composememoapp.domain.SaveMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val saveMemoUseCase: SaveMemoUseCase,
    private val getAllMemoUseCase: GetAllMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
    @AndroidMainScheduler private val androidSchedulers: Scheduler,
    @IOScheduler private val ioScheduler: Scheduler,
) : ViewModel() {

    private val _stateSource: PublishSubject<MemoState> = PublishSubject.create()
    private val _querySource: PublishSubject<String> = PublishSubject.create()
    private val _memoListSource: PublishSubject<List<MemoEntity>> = PublishSubject.create()
    private var _memoList: List<MemoEntity> = emptyList()

    val memoList: Observable<List<MemoEntity>> =
        Observable.zip(
            _memoListSource, _querySource,
            BiFunction { t1, t2 ->
                if (t2.isBlank() or t2.isNullOrEmpty()) {
                    t1
                } else {
                    t1.filter { it.contents.any { block -> block.content.contains(t2) } }
                }
            }
        ).publish().autoConnect()

    val state: Observable<MemoState> = _stateSource.publish().autoConnect()

    init {
        _querySource.debounce(500L, TimeUnit.MILLISECONDS)
    }

    fun saveMemo(memoEntity: MemoEntity?, contents: List<ContentBlock<*>>, tags: List<String>) {
        val memo = sortContentBlocks(memoEntity = memoEntity, contents = contents, tags)
        saveMemoUseCase(memoEntity = memo)
            .subscribeOn(ioScheduler)
            .observeOn(androidSchedulers)
            .subscribe(
                { handleSuccess(MemoState.SaveSuccess) },
                { error -> handleError(error.message) }
            )
    }

    fun getMemo(memoId: Long) = _memoList.find { it.id == memoId }

    fun getAllMemo() {
        getAllMemoUseCase()
            .subscribeOn(ioScheduler)
            .observeOn(androidSchedulers)
            .subscribe { memos ->
                _querySource.onNext("")
                _memoListSource.onNext(memos)
                _memoList = memos
            }
    }

    fun searchMemo(word: String) {
        _querySource.onNext(word)
    }

    fun deleteMemo(memoEntity: MemoEntity) {
        deleteMemoUseCase(memoEntity)
            .subscribeOn(ioScheduler)
            .observeOn(androidSchedulers)
            .subscribe(
                { handleSuccess(MemoState.DeleteSuccess) },
                { error -> handleError(error.message) }
            )
    }

    fun sortContentBlocks(
        memoEntity: MemoEntity?,
        contents: List<ContentBlock<*>>,
        tags: List<String>
    ): MemoEntity {
        val contentBlocks = contents
            .asSequence()
            .map { block ->
                block.convertToContentBlockEntity()
            }
            .filter { block ->
                block.content.isNotBlank()
            }
            .mapIndexed { index, contentBlockEntity ->
                contentBlockEntity.seq = index + 1L
                contentBlockEntity
            }.toList()

        return memoEntity?.let {
            it.copy(contents = contentBlocks, tagEntities = tags)
        } ?: MemoEntity(contents = contentBlocks, tagEntities = tags)
    }

    private fun handleSuccess(state: MemoState) {
        Log.d("MemoViewModel", "handleSuccess : $state ")
        _stateSource.onNext(state)
    }

    private fun handleError(errorMsg: String?) {
        Log.d("MemoViewModel", "handleError : $errorMsg ")
        _stateSource.onNext(MemoState.Error(errorMsg ?: "에러가 발생했습니다."))
    }
}
