package com.nicer.attiary.data.report

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@ProvidedTypeConverter
class HashMapTypeConverter (private val gson: Gson) {
	@TypeConverter
	fun hashMapToJson(value: HashMap<String, Int>?): String? {
		return gson.toJson(value)
	}

	@TypeConverter
	fun jsonToHashMap(value: String?): HashMap<String, Int>? {
		return gson.fromJson(value,object : TypeToken<HashMap<String, Int>>() {}.type)
	}
}
