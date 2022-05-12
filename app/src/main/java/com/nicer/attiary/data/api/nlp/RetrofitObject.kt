package com.nicer.attiary.data.api.nlp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
	private fun getRetrofit(): Retrofit {
		return Retrofit.Builder()
			.baseUrl("http://43.155.140.77:5000/")
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	fun getApiService(): RetrofitService {
		return getRetrofit().create(RetrofitService::class.java)
	}
}