package com.example.composememoapp.viewModel

import com.example.composememoapp.data.ContentType
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.repository.MemoRepository
import com.example.composememoapp.domain.GetAllMemoUseCase
import com.example.composememoapp.domain.SaveMemoUseCase
import com.example.composememoapp.presentation.viewModel.MemoState
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.util.model.ContentBlocksState
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.kotlin.toFlowable
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.whenever

class MemoViewModelTest {

    // 메모를 카테고리에 따라 필터링한다.
    // 메모를 저장한다, 수정한다.
    // 메모를 삭제한다.

    private val testMemoRepository = Mockito.mock(MemoRepository::class.java)

    private val saveMemoUseCaseMock = SaveMemoUseCase(testMemoRepository)
    private val getAllMemoUseCase = GetAllMemoUseCase(testMemoRepository)

    private val schedulers = Schedulers.newThread()
    private val memoViewModel = MemoViewModel(
        ioScheduler = schedulers,
        saveMemoUseCase = saveMemoUseCaseMock,
        getAllMemoUseCase = getAllMemoUseCase,
        androidSchedulers = schedulers
    )

    private val memoEntityMock = MemoEntity(
        id = 1,
        contents = List(10) {
            ContentBlockEntity(
                type = ContentType.Text,
                seq = it.toLong(),
                content = "this is textBlock$it"
            )
        }
    )

    private val contentState =
        ContentBlocksState(memoEntityMock.contents.map { it.convertToContentBlockModel() }
            .toMutableList())

    private val memoListMock = List(20) {
        MemoEntity(
            id = it.toLong(),
            contents = List(10) {
                ContentBlockEntity(
                    type = ContentType.Text,
                    seq = it.toLong(),
                    content = "this is textBlock$it"
                )
            }
        )
    }

    @Test
    @DisplayName("메모를 저장 성공시 저장 성공 상태를 발행한다.")
    fun insertMemoSuccessTest() {
        given(testMemoRepository.insertMemo(memoEntity = any()))
            .willReturn(Completable.complete())

        val testObserver: TestObserver<MemoState> = TestObserver()
        memoViewModel
            .state
            .subscribe(testObserver)

        memoViewModel.saveMemo(memoEntityMock, contentState.contents)

        testObserver.awaitCount(1)
        assertThat(testObserver.values().first()).isEqualTo(MemoState.SaveSuccess)

    }

    @Test
    @DisplayName("메모를 저장 실패시 저장 실패 상태를 발행한다.")
    fun insertMemoFailTest() {
        given(testMemoRepository.insertMemo(memoEntity = any()))
            .willReturn(Completable.error(Throwable("메모 저장 에러")))

        val testObserver: TestObserver<MemoState> = TestObserver()
        memoViewModel
            .state
            .subscribe(testObserver)

        memoViewModel.saveMemo(memoEntityMock, contents =contentState.contents )

        testObserver.awaitCount(1)
        assertThat(testObserver.values().first()).isEqualTo(MemoState.Error("메모 저장 에러"))
    }

    @Test
    @DisplayName("저장되어 있는 메모 전체를 가져온다.")
    fun getAllMemoTest() {
        given(testMemoRepository.getAllMemo())
            .willReturn(listOf(memoListMock).toFlowable())

        memoViewModel.getAllMemo()
        memoViewModel.memoList.test().await().assertValue(memoListMock)
    }
}
