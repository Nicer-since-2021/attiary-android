package com.nicer.attiary.data.diary

import android.content.Context

class DiaryList(context: Context) {
	private var sharedPref = context.getSharedPreferences("Diary", Context.MODE_PRIVATE)
	// 목록에 일기 추가
	fun addDiary(rDate : Long, emotion : String) {
		sharedPref.edit().apply {
			putString(rDate.toString(), emotion)
			apply()
		}
	}

	// 목록에서 해당 날짜 일기 제거
	fun removeDiary(rDate : Long) {
		sharedPref.edit().apply {
			remove(rDate.toString())
			apply()
		}
	}

	// 해당 날짜 일기가 있는가?
	fun isExist(rDate : Long): Boolean {
		if (sharedPref.contains(rDate.toString())) {
			return true
		}
		return false
	}


}