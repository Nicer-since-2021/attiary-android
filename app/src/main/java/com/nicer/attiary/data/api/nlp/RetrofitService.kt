package com.nicer.attiary.data.api.nlp

import com.nicer.attiary.data.api.nlp.chat.Chat
import com.nicer.attiary.data.api.nlp.classification.Classification
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
//    @GET("chatbot/v2/")
//    fun getUser(): Call<User>

    @GET("chatbot/b")
    fun getChatRes(
        @Query("s") s: String
    ): Call<Chat>

    @GET("diary")
    fun getReport(
        @Query("s") s: String
    ) : Call<Classification>

}