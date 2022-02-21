package com.nicer.attiary.data.report

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "report")
data class Report(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "date")
    var rDate: Long? = null,
    var firstEmotion: String="",
    var firstPercent: Int =0,
    var secondEmotion: String ="",
    var secondPercent: Int =0,
    var thirdEmotion: String ="",
    var thirdPercent: Int = 0,
    var fourthEmotion: String = "",
    var fourthPercent: Int = 0
) {

}

//@Entity
//class Repot {
//    @PrimaryKey(autoGenerate = true)
//
//    @ColumnInfo(name = "first_emotion")
//    var firstEmotion: String=""
//    @ColumnInfo(name = "first_percent")
//    var firstPercent: Int =0
//    @ColumnInfo(name = "second_emotion")
//    var secondEmotion: String =""
//    @ColumnInfo(name = "second_percent")
//    var secondPercent: Int =0
//    @ColumnInfo(name = "third_emotion")
//    var thirdEmotion: String =""
//    @ColumnInfo(name = "third_percent")
//    var thirdPercent: Int = 0
//    @ColumnInfo(name = "fourth_emotion")
//    var fourthEmotion: String = ""
//    @ColumnInfo(name = "fourth_percent")
//    var fourthPercent: Int = 0
//
//    /*
//    화 = ag
//    슬픔 = s
//    불안 = ax
//    피곤 = t
//    후회 = r
//    기쁨 = ha
//    희망 = ho
//    */
//    constructor()
//
//
//    class Builder(
//        val date: Long
//    ) {
//
//        var firstEmotion = ""
//        var firstPercent = 0
//        var secondEmotion = ""
//        var secondPercent = 0
//        var thirdEmotion = ""
//        var thirdPercent = 0
//        var fourthEmotion = ""
//        var fourthPercent = 0
//
//        fun firstEmotion(firstEmotion: String): Builder {
//            this.firstEmotion = firstEmotion
//            return this
//        }
//        fun firstPercent(firstPercent: Int): Builder {
//            this.firstPercent = firstPercent
//            return this
//        }
//        fun secondEmotion(secondEmotion: String): Builder {
//            this.secondEmotion = secondEmotion
//            return this
//        }
//        fun thirdPercent(thirdPercent: Int): Builder {
//            this.thirdPercent = thirdPercent
//            return this
//        }
//        fun thirdEmotion(thirdEmotion: String): Builder {
//            this.thirdEmotion = thirdEmotion
//            return this
//        }
//        fun fourthPercent(fourthPercent: Int): Builder {
//            this.fourthPercent = fourthPercent
//            return this
//        }
//        fun fourthEmotion(fourthEmotion: String): Builder {
//            this.fourthEmotion = fourthEmotion
//            return this
//        }
//        fun secondPercent(secondPercent: Int): Builder {
//            this.secondPercent = secondPercent
//            return this
//        }
//
//        fun build(): Report {
//            return Report(this)
//        }
//    }
//
//    constructor(builder: Builder) {
//        rDate=builder.date
//        firstEmotion = builder.firstEmotion
//        firstPercent = builder.firstPercent
//        secondEmotion = builder.secondEmotion
//        secondPercent = builder.secondPercent
//        thirdEmotion = builder.thirdEmotion
//        thirdPercent = builder.thirdPercent
//        fourthEmotion = builder.fourthEmotion
//        fourthPercent = builder.fourthPercent
//    }
//
//}