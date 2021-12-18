package com.example.composememoapp.viewModel

import com.example.composememoapp.data.ContentType
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.repository.MemoAppRepository
import com.example.composememoapp.domain.DeleteMemoUseCase
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
import java.util.concurrent.TimeUnit

class MemoViewModelTest {

    // 메모를 카테고리에 따라 필터링한다.
    // 메모를 저장한다, 수정한다.
    // 메모를 삭제한다.

    private val testMemoRepository = Mockito.mock(MemoAppRepository::class.java)

    private val saveMemoUseCaseMock = SaveMemoUseCase(testMemoRepository)
    private val getAllMemoUseCase = GetAllMemoUseCase(testMemoRepository)
    private val deleteMemoUseCase = DeleteMemoUseCase(testMemoRepository)

    private val schedulers = Schedulers.newThread()

    private val memoEntityMock = MemoEntity(
        id = 1,
        contents = List(5) {
            ContentBlockEntity(
                type = ContentType.Text,
                seq = it.toLong(),
                content = "this is textBlock$it"
            )
        },
        tagEntities = listOf("hi", "hello")
    )

    private val contentState =
        ContentBlocksState(
            memoEntityMock.contents.map { it.convertToContentBlockModel() }
                .toMutableList()
        )

    private val memoListMock = List(5) {
        MemoEntity(
            id = it.toLong(),
            contents = List(5) {
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
        val memoViewModel = MemoViewModel(
            ioScheduler = schedulers,
            saveMemoUseCase = saveMemoUseCaseMock,
            getAllMemoUseCase = getAllMemoUseCase,
            deleteMemoUseCase = deleteMemoUseCase,
            androidSchedulers = schedulers
        )

        given(testMemoRepository.insertMemo(memoEntity = any()))
            .willReturn(Completable.complete())

        val testObserver: TestObserver<MemoState> = TestObserver()
        memoViewModel
            .state
            .subscribe(testObserver)

        memoViewModel.saveMemo(
            memoEntityMock,
            contentState.contents,
            tags = memoEntityMock.tagEntities
        )

        testObserver.awaitCount(1)
        assertThat(testObserver.values().first()).isEqualTo(MemoState.SaveSuccess)
    }

    @Test
    @DisplayName("메모를 저장 실패시 저장 실패 상태를 발행한다.")
    fun insertMemoFailTest() {
        val memoViewModel = MemoViewModel(
            ioScheduler = schedulers,
            saveMemoUseCase = saveMemoUseCaseMock,
            getAllMemoUseCase = getAllMemoUseCase,
            deleteMemoUseCase = deleteMemoUseCase,
            androidSchedulers = schedulers
        )

        given(testMemoRepository.insertMemo(memoEntity = any()))
            .willReturn(Completable.error(Throwable("메모 저장 에러")))

        val testObserver: TestObserver<MemoState> = TestObserver()
        memoViewModel
            .state
            .subscribe(testObserver)

        memoViewModel.saveMemo(
            memoEntityMock,
            contents = contentState.contents,
            tags = memoEntityMock.tagEntities
        )

        testObserver.awaitCount(1)
        assertThat(testObserver.values().first()).isEqualTo(MemoState.Error("메모 저장 에러"))
    }

    @Test
    @DisplayName("저장되어 있는 메모 전체를 가져온다.")
    fun getAllMemoTest() {
        val memoViewModel = MemoViewModel(
            ioScheduler = schedulers,
            saveMemoUseCase = saveMemoUseCaseMock,
            getAllMemoUseCase = getAllMemoUseCase,
            deleteMemoUseCase = deleteMemoUseCase,
            androidSchedulers = schedulers
        )

        given(testMemoRepository.getAllMemo()).willReturn(listOf(memoListMock).toFlowable())

        memoViewModel.getAllMemo()

        memoViewModel.memoList.test().awaitCount(2).assertValue(memoListMock)
    }

    @Test
    @DisplayName("검색어 1이 포함된 메모만 리스트에 표시한다.")
    fun filterMemoList() {

        val memoViewModel = MemoViewModel(
            ioScheduler = schedulers,
            saveMemoUseCase = saveMemoUseCaseMock,
            getAllMemoUseCase = getAllMemoUseCase,
            deleteMemoUseCase = deleteMemoUseCase,
            androidSchedulers = schedulers
        )

        whenever(testMemoRepository.getAllMemo())
            .thenReturn(listOf(memoListMock).toFlowable())

        val expected = memoListMock.filter {
            it.contents.any { block -> block.content.contains("1") }
        }

        memoViewModel.getAllMemo()
        memoViewModel.searchMemo("1")

        val actual =
            memoViewModel.memoList.test().awaitDone(1000, TimeUnit.MILLISECONDS).values().first()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @DisplayName("메모 삭제 성공 시 삭제 성공 상태를 발행한다.")
    fun deleteMemoSuccessTest() {
        val memoViewModel = MemoViewModel(
            ioScheduler = schedulers,
            saveMemoUseCase = saveMemoUseCaseMock,
            getAllMemoUseCase = getAllMemoUseCase,
            deleteMemoUseCase = deleteMemoUseCase,
            androidSchedulers = schedulers
        )

        given(testMemoRepository.deleteMemo(any()))
            .willReturn(Completable.complete())

        val testObserver: TestObserver<MemoState> = TestObserver()
        memoViewModel
            .state
            .subscribe(testObserver)

        memoViewModel.deleteMemo(memoEntityMock)

        testObserver.awaitCount(1)
        assertThat(testObserver.values().first()).isEqualTo(MemoState.DeleteSuccess)
    }

    @Test
    @DisplayName("메모 삭제 실패 시 삭제 실패 상태 를 발행한다.")
    fun deleteMemoFailTest() {
        val memoViewModel = MemoViewModel(
            ioScheduler = schedulers,
            saveMemoUseCase = saveMemoUseCaseMock,
            getAllMemoUseCase = getAllMemoUseCase,
            deleteMemoUseCase = deleteMemoUseCase,
            androidSchedulers = schedulers
        )

        given(testMemoRepository.deleteMemo(any()))
            .willReturn(Completable.error(Exception("메모 삭제 실패")))

        val testObserver: TestObserver<MemoState> = TestObserver()
        memoViewModel
            .state
            .subscribe(testObserver)

        memoViewModel.deleteMemo(memoEntityMock)

        testObserver.awaitCount(1)
        assertThat(testObserver.values().first()).isEqualTo(MemoState.Error("메모 삭제 실패"))
    }

    @Test
    @DisplayName("메모 아이디에 해당하는 메모를 찾아서 리턴한다.")
    fun findMemoByMemoId() {
        val memoViewModel = MemoViewModel(
            ioScheduler = schedulers,
            saveMemoUseCase = saveMemoUseCaseMock,
            getAllMemoUseCase = getAllMemoUseCase,
            deleteMemoUseCase = deleteMemoUseCase,
            androidSchedulers = schedulers
        )

        whenever(testMemoRepository.getAllMemo())
            .thenReturn(listOf(memoListMock).toFlowable())

        memoViewModel.getAllMemo()

        Thread.sleep(500)

        val actual = memoViewModel.getMemo(1L)

        assertThat(actual).isEqualTo(memoListMock.find { it.id == 1L })
    }
}
