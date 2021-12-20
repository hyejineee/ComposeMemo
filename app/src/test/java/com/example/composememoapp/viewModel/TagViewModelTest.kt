package com.example.composememoapp.viewModel

import com.example.composememoapp.data.database.entity.TagEntity
import com.example.composememoapp.data.repository.MemoAppRepository
import com.example.composememoapp.domain.GetAllTagUseCase
import com.example.composememoapp.presentation.viewModel.TagViewModel
import io.reactivex.rxjava3.kotlin.toFlowable
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.given

class TagViewModelTest {

    private val testMemoRepository = Mockito.mock(MemoAppRepository::class.java)

    private val schedulers = Schedulers.newThread()
    private val getAllTagUseCase = GetAllTagUseCase(testMemoRepository)
    private val tagViewModel = TagViewModel(
        ioScheduler = schedulers,
        androidScheduler = schedulers,
        getAllTagUseCase = getAllTagUseCase
    )

    private val tagListMock = List(5){
        TagEntity(tag = "tag$it")
    }

    @Test
    @DisplayName("모든 태그를 가져온다.")
    fun getAllTagTest(){
        given(testMemoRepository.getAllTag()).willReturn(listOf(tagListMock).toFlowable())

        tagViewModel.getAllTag()

        tagViewModel.tagList.test().assertValue(tagListMock)
    }

}
