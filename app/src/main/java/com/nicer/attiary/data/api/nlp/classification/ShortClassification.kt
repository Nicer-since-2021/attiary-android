package com.nicer.attiary.data.api.nlp.classification

import com.google.gson.annotations.SerializedName

data class ShortClassification(
    @SerializedName("emotion_no")
    val emotion_no: Int,
)