package com.nicer.attiary.data.report

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "report")
data class Report(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "date")
    var rDate: Long? = null,
    var diaryContent: String="",
    var representative: String = "",
    @TypeConverters(HashMapTypeConverter::class)
    var emotions: HashMap<String,Int>,
    var happiness: Int,
    var depression: Int,
    var commentFromAtti: String =""
)