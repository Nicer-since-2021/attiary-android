package com.nicer.attiary.data.report

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "report")
class Report {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "report_id")
    var reportId: Long? = null

    @ColumnInfo(name = "date")
    var rDate: String? = null

    var diaryContent: String=""

    var representative: String = ""

    @TypeConverters(HashMapTypeConverter::class)
    lateinit var emotions: HashMap<String,Int>

    var happiness: Int = 0

    var depression: Int = 0

    var commentFromAtti: String =""

    constructor()

    class Builder(
        val rDate: String, val diaryContent: String, val representative: String,
        val emotions:HashMap<String, Int>, val happiness: Int,
        val depression: Int, val commentFromAtti: String) {

        fun build(): Report {
            return Report(this)
        }
    }

    constructor(builder: Builder) {
        rDate = builder.rDate
        diaryContent = builder.diaryContent
        representative = builder.representative
        emotions = builder.emotions
        happiness = builder.happiness
        depression = builder.depression
        commentFromAtti = builder.commentFromAtti
    }
}