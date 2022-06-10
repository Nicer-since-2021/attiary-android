package com.nicer.attiary.data.api.nlp

import com.nicer.attiary.data.api.nlp.chat.Chat
import com.nicer.attiary.data.api.nlp.classification.Classification
import com.nicer.attiary.data.api.nlp.classification.ShortClassification
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("chatbot/b")
    fun getKoBERTChatRes(
        @Query("s") s: String
    ): Call<Chat>

    @GET("chatbot/g")
    fun getKoGPT2ChatRes(
        @Query("s") s: String
    ): Call<Chat>

    @GET("emotion")
    fun getChatEmo(
        @Query("s") s: String
    ): Call<ShortClassification>

    @GET("diary")
    fun getReport(
        @Query("s") s: String
    ): Call<Classification>
}