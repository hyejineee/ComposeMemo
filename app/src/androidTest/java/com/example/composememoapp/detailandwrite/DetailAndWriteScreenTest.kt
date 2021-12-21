package com.example.composememoapp.detailandwrite

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.composememoapp.data.ContentType
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.database.entity.TagEntity
import com.example.composememoapp.data.repository.MemoAppRepository
import com.example.composememoapp.domain.DeleteMemoUseCase
import com.example.composememoapp.domain.GetAllMemoUseCase
import com.example.composememoapp.domain.GetAllTagUseCase
import com.example.composememoapp.domain.SaveMemoUseCase
import com.example.composememoapp.presentation.theme.ComposeMemoAppTheme
import com.example.composememoapp.presentation.ui.detailandwrite.DetailAndWriteScreen
import com.example.composememoapp.presentation.viewModel.MemoViewModel
import com.example.composememoapp.presentation.viewModel.TagViewModel
import io.reactivex.rxjava3.kotlin.toFlowable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.given

@ExperimentalComposeUiApi
@RunWith(AndroidJUnit4::class)
class DetailAndWriteScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val testMemoRepository = Mockito.mock(MemoAppRepository::class.java)

    private lateinit var memoViewModel: MemoViewModel
    private lateinit var tagViewModel: TagViewModel

    private val memo = MemoEntity(
        id = 0,
        contents = List(10) {
            ContentBlockEntity(
                type = ContentType.Text,
                seq = it.toLong(),
                content = "this is text block content $it"
            )
        }
    )

    @Before
    fun init() {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.mainClock.advanceTimeBy(50L)

        val scheduler = Schedulers.newThread()

        val saveMemoUseCaseMock = SaveMemoUseCase(testMemoRepository)
        val getAllMemoUseCase = GetAllMemoUseCase(testMemoRepository)
        val deleteMemoUseCase = DeleteMemoUseCase(testMemoRepository)

        val getAllTagUseCase = GetAllTagUseCase(testMemoRepository)

        given(testMemoRepository.getAllMemo()).willReturn(emptyList<List<MemoEntity>>().toFlowable())
        given(testMemoRepository.getAllTag()).willReturn(emptyList<List<TagEntity>>().toFlowable())

        tagViewModel = TagViewModel(
            ioScheduler = scheduler,
            androidScheduler = scheduler,
            getAllTagUseCase = getAllTagUseCase
        )

        memoViewModel = MemoViewModel(
            ioScheduler = scheduler,
            saveMemoUseCase = saveMemoUseCaseMock,
            getAllMemoUseCase = getAllMemoUseCase,
            deleteMemoUseCase = deleteMemoUseCase,
            androidSchedulers = scheduler
        )
    }

    private fun setContentWithDetailAndWriteScreen(
        memoEntity: MemoEntity? = null
    ) {

        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                DetailAndWriteScreen(
                    memoEntity = memoEntity,
                    memoViewModel = memoViewModel,
                    tagViewModel = tagViewModel,
                    handleBackButtonClick = {},
                )
            }
        }
    }

    @Test
    fun 메모_상세보기_모드일때_메모의_내용을_보여준다() {

        setContentWithDetailAndWriteScreen(
            memoEntity = memo,
        )

        composeTestRule
            .onNodeWithText(memo.contents.first().content)
            .assertIsDisplayed()
    }

    @Test
    fun 새로운_메모를_작성하는_모드일_때_화면을_클릭하면_바로_메로를_작성할_수_있다() {
        setContentWithDetailAndWriteScreen()

        composeTestRule
            .onNode(hasAnyChild(hasSetTextAction()))
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("text block 1", useUnmergedTree = true)
            .performTextInput("hello")
        composeTestRule.mainClock.advanceTimeBy(50L)

        composeTestRule
            .onNodeWithContentDescription("text block 1", useUnmergedTree = true)
            .assertTextEquals("hello")
    }
}
