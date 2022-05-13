package com.nicer.attiary.util

import android.graphics.Color

enum class Emotion(val num: Int, val en: String, val kr: String, val color: Int) {
    JOY(0, "joy", "기쁨", Color.rgb(182, 219, 176)),
    HOPE(1, "hope", "희망", Color.rgb(249, 198, 234)),
    NEUTRALITY(2, "neutrality", "중립", Color.rgb(216, 216, 216)),
    ANGER(3, "anger", "분노", Color.rgb(252, 179, 169)),
    SADNESS(4, "sadness", "슬픔", Color.rgb(169, 197, 252)),
    ANXIETY(5, "anxiety", "불안", Color.rgb(190, 169, 252)),
    TIREDNESS(6, "tiredness", "피곤", Color.rgb(252, 209, 169)),
    REGRET(7, "regret", "후회", Color.rgb(211, 172, 126));

    companion object {
        public fun toKr(str: String): String {
            return if (JOY.en == str) {
                JOY.kr
            } else if (HOPE.en == str) {
                HOPE.kr
            } else if (NEUTRALITY.en == str) {
                NEUTRALITY.kr
            } else if (ANGER.en == str) {
                ANGER.kr
            } else if (SADNESS.en == str) {
                SADNESS.kr
            } else if (ANXIETY.en == str) {
                ANXIETY.kr
            } else if (TIREDNESS.en == str) {
                TIREDNESS.kr
            } else {
                REGRET.kr
            }
        }

        fun toColor(str: String): Int {
            return if (JOY.en == str) {
                JOY.color
            } else if (HOPE.en == str) {
                HOPE.color
            } else if (NEUTRALITY.en == str) {
                NEUTRALITY.color
            } else if (ANGER.en == str) {
                ANGER.color
            } else if (SADNESS.en == str) {
                SADNESS.color
            } else if (ANXIETY.en == str) {
                ANXIETY.color
            } else if (TIREDNESS.en == str) {
                TIREDNESS.color
            } else {
                REGRET.color
            }
        }
    }
}


