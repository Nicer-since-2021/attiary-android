package com.nicer.attiary.data.report

import androidx.annotation.NonNull
import androidx.room.*
import org.jetbrains.annotations.NotNull


@Entity(tableName = "report", indices = [Index(value = arrayOf("date"), unique = true)])
class Report {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "report_id")
    var reportId: Long = 0

    @NotNull
    @ColumnInfo(name = "date")
    var rDate: String = ""

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