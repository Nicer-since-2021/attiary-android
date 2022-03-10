package com.nicer.attiary.data.report

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "report")
data class Report(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "date")
    var rDate: Long? = null,
    var diaryContent : String="",
    var firstEmotion: String="",
    var firstPercent: Int =0,
    var secondEmotion: String ="",
    var secondPercent: Int =0,
    var thirdEmotion: String ="",
    var thirdPercent: Int = 0,
    var fourthEmotion: String = "",
    var fourthPercent: Int = 0,
    var commentFromAtti: String =""
)