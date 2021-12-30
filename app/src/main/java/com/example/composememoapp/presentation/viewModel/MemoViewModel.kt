package com.example.composememoapp.presentation.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.composememoapp.data.MemoModel
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.di.AndroidMainScheduler
import com.example.composememoapp.di.IOScheduler
import com.example.composememoapp.domain.DeleteMemoUseCase
import com.example.composememoapp.domain.GetAllMemoUseCase
import com.example.composememoapp.domain.SaveMemoUseCase
import com.example.composememoapp.presentation.ui.component.blocks.ImageBlock
import com.example.composememoapp.presentation.ui.component.blocks.TextBlock
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Function4
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
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
    private val _querySource: BehaviorSubject<String> = BehaviorSubject.create()
    private val _favoriteSource: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val _memoListSource: BehaviorSubject<List<MemoEntity>> = BehaviorSubject.create()
    private val _tagSource: BehaviorSubject<String> = BehaviorSubject.create()

    private var _memoList: List<MemoEntity> = emptyList()

    val memoList: Observable<List<MemoEntity>> =
        Observable.combineLatest(
            _memoListSource,
            _querySource,
            _tagSource,
            _favoriteSource,
            Function4 { list: List<MemoEntity>, query: String, tag: String, favorite: Boolean ->
                list.asSequence()
                    .filter { memo ->
                        if (query.isNotBlank()) {
                            memo.contents.any { block -> block.content.contains(query) }
                        } else {
                            true
                        }
                    }
                    .filter { memo ->
                        if (tag != "ALL") {
                            memo.tagEntities.any { it == tag }
                        } else {
                            true
                        }
                    }
                    .filter { memo ->
                        if (favorite) {
                            memo.isBookMarked == favorite
                        } else {
                            true
                        }
                    }
                    .sortedByDescending { it.updatedDate }
                    .toList()
            }
        )
    val state: Observable<MemoState> = _stateSource.publish().autoConnect()

    init {
        _querySource.debounce(500L, TimeUnit.MILLISECONDS)

        _tagSource.onNext("ALL")
        _querySource.onNext("")
        _favoriteSource.onNext(false)

        memoList.subscribe {
            _stateSource.onNext(MemoState.FetchSuccess)
        }

        getAllMemo()
    }

    fun saveMemo(memoModel: MemoModel, context: Context) {
        handleLoadingState()

        Single.create<MemoEntity> { obervable ->
            val converted = memoModel.contents
                .asSequence()
                .map {
                    when (it) {
                        is TextBlock -> it.content = it.textInputState.value.text
                        is ImageBlock ->
                            it.content =
                                if (memoModel.id == null || it.content?.scheme != "file") saveImage(
                                    bitmap = it.imageState.value,
                                    context = context
                                ) else it.content
                    }
                    it.convertToContentBlockEntity()
                }.mapIndexed { index, contentBlockEntity ->
                    contentBlockEntity.seq = index + 1L
                    contentBlockEntity
                }.toList()

            val memoEntity = MemoEntity(
                id = memoModel.id,
                updatedDate = memoModel.updatedDate,
                contents = converted,
                isBookMarked = memoModel.isBookMarked,
                tagEntities = memoModel.tagEntities
            )

            obervable.onSuccess(memoEntity)
        }.concatMapCompletable {
            saveMemoUseCase(memoEntity = it)
        }.subscribeOn(ioScheduler)
            .observeOn(androidSchedulers)
            .subscribe(
                { handleSuccess(MemoState.SaveSuccess) },
                { error -> handleError(error.message) }
            )
    }

    fun getMemo(memoId: Long) = _memoList.find { it.id == memoId }

    fun getAllMemo() {
        handleLoadingState()

        getAllMemoUseCase()
            .subscribeOn(ioScheduler)
            .observeOn(androidSchedulers)
            .subscribe { memos ->
                _memoListSource.onNext(memos)
                _memoList = memos
            }
    }

    fun searchMemo(word: String) {
        _querySource.onNext(word)
    }

    fun deleteMemo(memoEntity: MemoEntity) {
        handleLoadingState()

        deleteMemoUseCase(memoEntity)
            .subscribeOn(ioScheduler)
            .observeOn(androidSchedulers)
            .subscribe(
                { handleSuccess(MemoState.DeleteSuccess) },
                { error -> handleError(error.message) }
            )
    }

    fun filterMemoByTag(tag: String) {
        _tagSource.onNext(tag)
    }

    fun filterMemoByFavorite(isFavorite: Boolean) {
        _favoriteSource.onNext(isFavorite)
    }

    private fun handleLoadingState() {
        _stateSource.onNext(MemoState.Loading)
    }

    private fun handleSuccess(state: MemoState) {
        Log.d("MemoViewModel", "handleSuccess : $state ")
        _stateSource.onNext(state)
    }

    private fun handleError(errorMsg: String?) {
        Log.d("MemoViewModel", "handleError : $errorMsg ")
        _stateSource.onNext(MemoState.Error(errorMsg ?: "에러가 발생했습니다."))
    }

    private fun saveImage(bitmap: Bitmap?, context: Context): Uri {
        val imageName = System.currentTimeMillis().toString() + ".jpeg"
        val dirName = "images"

        val createdImage = context.filesDir.let {
            val dir = File(it.path, dirName)
            if (dir.exists().not()) {
                dir.mkdirs()
            }

            val file = File(dir, imageName)

            try {
                val out = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            file
        }

        return Uri.fromFile(createdImage)
    }
}
