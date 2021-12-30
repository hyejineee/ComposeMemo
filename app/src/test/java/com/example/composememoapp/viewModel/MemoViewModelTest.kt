package com.example.composememoapp.viewModel

import android.content.Context
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.repository.MemoAppRepository
import com.example.composememoapp.domain.DeleteMemoUseCase
import com.example.composememoapp.domain.GetAllMemoUseCase
import com.example.composememoapp.domain.SaveMemoUseCase
import com.example.composememoapp.presentation.ui.component.blocks.ContentType
import com.example.composememoapp.presentation.viewModel.MemoState
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.kotlin.toFlowable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import java.util.concurrent.TimeUnit

class MemoViewModelTest {

    // 메모를 카테고리에 따라 필터링한다.
    // 메모를 저장한다, 수정한다.
    // 메모를 삭제한다.

    private val testMemoRepository = Mockito.mock(MemoAppRepository::class.java)
    private val context = Mockito.mock(Context::class.java)

    private lateinit var memoViewModel: MemoViewModel

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

    private val memoListMock = List(5) {
        MemoEntity(
            id = it.toLong(),
            contents = List(5) {
                ContentBlockEntity(
                    type = ContentType.Text,
                    seq = it.toLong(),
                    content = "this is textBlock$it"
                )
            },
            isBookMarked = it == 1,
            tagEntities = if (it == 1) listOf("hi", "hello") else emptyList()
        )
    }

    @BeforeEach
    fun init() {
        val saveMemoUseCaseMock = SaveMemoUseCase(testMemoRepository)
        val getAllMemoUseCase = GetAllMemoUseCase(testMemoRepository)
        val deleteMemoUseCase = DeleteMemoUseCase(testMemoRepository)

        val schedulers = Schedulers.newThread()

        given(testMemoRepository.getAllMemo()).willReturn(listOf(memoListMock).toFlowable())

        memoViewModel = MemoViewModel(
            ioScheduler = schedulers,
            saveMemoUseCase = saveMemoUseCaseMock,
            getAllMemoUseCase = getAllMemoUseCase,
            deleteMemoUseCase = deleteMemoUseCase,
            androidSchedulers = schedulers
        )
    }

    @Test
    @DisplayName("메모를 저장 성공시 저장 성공 상태를 발행한다.")
    fun insertMemoSuccessTest() {
        given(testMemoRepository.insertMemo(memoEntity = any()))
            .willReturn(Completable.complete())

        memoViewModel.saveMemo(
            memoEntityMock.convertToMemoViewModel(),
            context = context
        )

        val values = memoViewModel.state.test().awaitDone(500, TimeUnit.MILLISECONDS).values()
        assertThat(values).contains(MemoState.SaveSuccess)
    }

    @Test
    @DisplayName("메모를 저장 실패시 저장 실패 상태를 발행한다.")
    fun insertMemoFailTest() {
        given(testMemoRepository.insertMemo(memoEntity = any()))
            .willReturn(Completable.error(Throwable("메모 저장 에러")))

        memoViewModel.saveMemo(
            memoEntityMock.convertToMemoViewModel(),
            context = context
        )

        val values = memoViewModel.state.test().awaitDone(500, TimeUnit.MILLISECONDS).values()
        assertThat(values).contains(MemoState.Error("메모 저장 에러"))
    }

    @Test
    @DisplayName("저장되어 있는 메모 전체를 가져온다.")
    fun getAllMemoTest() {

        memoViewModel.memoList.test().awaitCount(1).assertValue(memoListMock)
    }

    @Test
    @DisplayName("검색어 1이 포함된 메모만 리스트에 표시한다.")
    fun filterMemoList() {
        val expected = memoListMock.filter {
            it.contents.any { block -> block.content.contains("1") }
        }.sortedByDescending { it.updatedDate }

        memoViewModel.searchMemo("1")

        val actual =
            memoViewModel.memoList.test().awaitDone(1000, TimeUnit.MILLISECONDS).values().first()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    @DisplayName("메모 삭제 성공 시 삭제 성공 상태를 발행한다.")
    fun deleteMemoSuccessTest() {
        given(testMemoRepository.deleteMemo(any()))
            .willReturn(Completable.complete())

        memoViewModel.deleteMemo(memoEntityMock)

        val values = memoViewModel.state.test().awaitDone(500, TimeUnit.MILLISECONDS).values()
        assertThat(values).contains(MemoState.DeleteSuccess)
    }

    @Test
    @DisplayName("메모 삭제 실패 시 삭제 실패 상태 를 발행한다.")
    fun deleteMemoFailTest() {
        given(testMemoRepository.deleteMemo(any()))
            .willReturn(Completable.error(Exception("메모 삭제 실패")))

        memoViewModel.deleteMemo(memoEntityMock)

        val values = memoViewModel.state.test().awaitDone(500, TimeUnit.MILLISECONDS).values()
        assertThat(values).contains(MemoState.Error("메모 삭제 실패"))
    }

    @Test
    @DisplayName("메모 아이디에 해당하는 메모를 찾아서 리턴한다.")
    fun findMemoByMemoId() {
        Thread.sleep(500)

        val actual = memoViewModel.getMemo(1L)

        assertThat(actual).isEqualTo(memoListMock.find { it.id == 1L })
    }

    @Test
    @DisplayName("북마크된 메모만 찾아서 메모 리스트에 표시한다.")
    fun filterMemoListByBookmarkedTest() {

        memoViewModel.filterMemoByFavorite(true)

        memoViewModel.memoList.test().awaitCount(1)
            .assertValue(memoListMock.filter { it.isBookMarked })
    }

    @Test
    @DisplayName("해당 태그가 있는 메모만 찾아서 메모 리스트에 표시한다.")
    fun filterMemoListByTagTest() {
        memoViewModel.filterMemoByTag("hi")

        memoViewModel.memoList.test().awaitCount(1)
            .assertValue(memoListMock.filter { memo -> memo.tagEntities.any { it == "hi" } })
    }
}
