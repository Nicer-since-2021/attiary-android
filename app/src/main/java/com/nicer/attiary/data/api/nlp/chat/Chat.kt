package com.nicer.attiary.data.api.nlp.chat

import com.google.gson.annotations.SerializedName

data class Chat(
    @SerializedName("answer")
    val answer: String,
)