package com.example.composememoapp.data.database

import androidx.room.TypeConverter
import com.example.composememoapp.data.database.entity.ContentBlockEntity
import com.example.composememoapp.presentation.ui.component.blocks.ContentType
import com.google.gson.Gson
import java.util.Date

class TypeConverters {
    @TypeConverter
    fun dateToLong(value: Date?) = value?.time?.toLong()
    @TypeConverter
    fun longToDate(value: Long?) = value?.let { Date(it) }

    @TypeConverter
    fun contentTypeToString(value: ContentType?) = value?.name
    @TypeConverter
    fun stringToContentType(value: String?) = when (value) {
        ContentType.Text.name -> ContentType.Text
        else -> ContentType.Unknown
    }

    @TypeConverter
    fun contentListToContentJson(value: List<ContentBlockEntity>?) = value?.let { Gson().toJson(it) }
    @TypeConverter
    fun contentJsonToContentList(value: String?) = value?.let {
        Gson().fromJson(value, Array<ContentBlockEntity>::class.java).toList()
    }

    @TypeConverter
    fun stringListToJson(value: List<String>?) = value?.let {
        Gson().toJson(it)
    }
    @TypeConverter
    fun jsonToStringList(value: String?) = value?.let {
        Gson().fromJson(it, Array<String>::class.java).toList()
    }
}
