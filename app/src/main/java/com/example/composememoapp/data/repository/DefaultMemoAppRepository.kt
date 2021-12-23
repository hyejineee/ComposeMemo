package com.example.composememoapp.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.example.composememoapp.data.ContentBlock
import com.example.composememoapp.data.ImageBlock
import com.example.composememoapp.data.MemoModel
import com.example.composememoapp.data.TextBlock
import com.example.composememoapp.data.database.MemoDao
import com.example.composememoapp.data.database.TagDao
import com.example.composememoapp.data.database.entity.MemoEntity
import com.example.composememoapp.data.database.entity.TagEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class DefaultMemoAppRepository @Inject constructor(
    private val memoDao: MemoDao,
    private val tagDao: TagDao
) : MemoAppRepository {

    override fun getAllMemo(): Flowable<List<MemoEntity>> =
        memoDao.getAllMemo()

    override fun insertMemo(memoModel: MemoModel,context: Context): Completable {

        val converted = memoModel.contents
            .asSequence()
            .map {
                when (it) {
                    is TextBlock -> it.content = it.textInputState.value.text
                    is ImageBlock -> it.content = saveImage(bitmap = it.imageState.value, context = context)
                }
                it.convertToContentBlockEntity()
            }
            .filter { block ->
                block.content.isNotBlank()
            }
            .mapIndexed { index, contentBlockEntity ->
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

        val tags = memoModel.tagEntities.map { TagEntity(tag = it) }


        return memoDao.insertMemoEntity(memoEntity = memoEntity)
            .mergeWith(tagDao.insertTagEntity(tags = tags))
    }

    override fun deleteMemo(memoEntity: MemoEntity): Completable =
        memoDao.deleteMemo(memoEntity = memoEntity)

    override fun getAllTag(): Flowable<List<TagEntity>> = tagDao.getAllTag()

    private fun saveImage(bitmap:Bitmap?, context: Context):Uri{
        val imageName = System.currentTimeMillis().toString()+".jpeg"
        val dirName = "images"

        val createdImage = context.filesDir.let {
            val dir = File(it.path , dirName)
            if(dir.exists().not()){
                dir.mkdirs()
            }

            val file = File(dir, imageName)

            try {
                val out = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.close()

            }catch (e:FileNotFoundException){
                e.printStackTrace()
            }catch (e:IOException){
                e.printStackTrace()
            }

            file
        }

        return Uri.fromFile(createdImage)
    }
}
