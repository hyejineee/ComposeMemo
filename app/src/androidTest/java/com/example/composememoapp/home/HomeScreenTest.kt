package com.example.composememoapp.home

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.composememoapp.R
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
import com.example.composememoapp.presentation.ui.home.HomeScreen
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

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var context: Context

    private val testMemoRepository = Mockito.mock(MemoAppRepository::class.java)

    private lateinit var memoViewModel: MemoViewModel
    private lateinit var tagViewModel: TagViewModel

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

    private val tagListMock = List(5) {
        TagEntity(tag = "tag $it")
    }

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.mainClock.advanceTimeBy(50L)

        val scheduler = Schedulers.newThread()
        val saveMemoUseCaseMock = SaveMemoUseCase(testMemoRepository)
        val getAllMemoUseCase = GetAllMemoUseCase(testMemoRepository)
        val deleteMemoUseCase = DeleteMemoUseCase(testMemoRepository)
        val getAllTagUseCase = GetAllTagUseCase(testMemoRepository)

        given(testMemoRepository.getAllMemo()).willReturn(listOf(memoListMock).toFlowable())
        given(testMemoRepository.getAllTag()).willReturn(emptyList<List<TagEntity>>().toFlowable())

        memoViewModel = MemoViewModel(
            ioScheduler = scheduler,
            saveMemoUseCase = saveMemoUseCaseMock,
            getAllMemoUseCase = getAllMemoUseCase,
            deleteMemoUseCase = deleteMemoUseCase,
            androidSchedulers = scheduler
        )

        tagViewModel = TagViewModel(
            ioScheduler = scheduler,
            androidScheduler = scheduler,
            getAllTagUseCase = getAllTagUseCase
        )
    }

    private fun setContentWithHomeScreen() {
        composeTestRule.setContent {
            ComposeMemoAppTheme() {
                HomeScreen(
                    handleClickMemoItem = {},
                    handleClickAddMemoButton = {},
                    memoViewModel = memoViewModel,
                    tagViewModel = tagViewModel
                )
            }
        }
    }

    @Test
    fun 홈_타이틀을_보여준다() {
        setContentWithHomeScreen()
        composeTestRule
            .onNodeWithText(context.getString(R.string.homeTitle))
            .assertIsDisplayed()
    }

    @Test
    fun 메모_검색창을_보여준다() {
        setContentWithHomeScreen()
        composeTestRule.mainClock.advanceTimeBy(50L)
        composeTestRule
            .onNodeWithText(context.getString(R.string.putSearchWordCaption))
            .assertIsDisplayed()
    }

    @Test
    fun 카테고리_선택_메뉴를_보여준다() {
        setContentWithHomeScreen()
        composeTestRule.mainClock.advanceTimeBy(50L)
        composeTestRule
            .onAllNodes(hasScrollAction())[0]
            .assertIsDisplayed()
    }

    @Test
    fun 저장된_메모를_보여준다() {
    }

    @Test
    fun 메모_추가_버튼을_누르면_메모_작성_화면으로_이동한다() {
    }
}
