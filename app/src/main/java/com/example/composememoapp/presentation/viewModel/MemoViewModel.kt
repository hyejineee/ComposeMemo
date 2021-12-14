package com.example.composememoapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.composememoapp.data.entity.MemoEntity
import com.example.composememoapp.domain.GetAllMemoUseCase
import com.example.composememoapp.domain.SaveMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val saveMemoUseCase: SaveMemoUseCase,
    private val getAllMemoUseCase : GetAllMemoUseCase
) : ViewModel() {

    private val statePublishSubject: PublishSubject<MemoState> = PublishSubject.create()
    val state = statePublishSubject.publish().autoConnect()

    fun saveMemo(memoEntity: MemoEntity) {
        saveMemoUseCase(memoEntity)
            .subscribe(
                { handleSuccess(MemoState.SaveSuccess) },
                { error -> handleError(error.message) }
            )
    }

    private fun handleSuccess(state:MemoState) {
        statePublishSubject.onNext(state)
    }

    private fun handleError(errorMsg: String?) {
        statePublishSubject.onNext(MemoState.Error(errorMsg ?: "에러가 발생했습니다."))
    }

    fun getAllMemo(){
        getAllMemoUseCase()
            .subscribe(
                {list -> handleSuccess(MemoState.GetAllMemoSuccess(list)) },
                {error -> handleError(error.message)}
            )
    }

}
