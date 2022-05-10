package com.nicer.attiary.nlp.chat

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
//    @GET("chatbot/v2/")
//    fun getUser(): Call<User>

    @GET("chatbot/v2")
    fun getChatRes(
        @Query("s") s: String
    ): Call<Chat>
}