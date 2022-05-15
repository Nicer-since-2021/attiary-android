package com.nicer.attiary.data.diary

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prolificinteractive.materialcalendarview.CalendarDay



class DiaryList(context: Context) {
	private var sharedPref = context.getSharedPreferences("Diary", Context.MODE_PRIVATE)
	// 목록에 일기 추가
	fun addDiary(rDate : String, emotion : String) {
		sharedPref.edit().apply {
			putString(rDate, emotion)
			apply()
		}
	}

	// 목록에서 해당 날짜 일기 제거
	fun removeDiary(rDate : String) {
		sharedPref.edit().apply {
			remove(rDate)
			apply()
		}
	}

	fun isExist(rDate : String?): Boolean {
		if (sharedPref.contains(rDate)) {
			return true
		}
		return false
	}

	fun isAnger(rDate : String?): Boolean {
		if (sharedPref.getString(rDate, "") == "anger") {
			return true
		}
		return false
	}

	fun isAnxiety(rDate : String?): Boolean {
		if (sharedPref.getString(rDate, "") == "anxiety") {
			return true
		}
		return false
	}

	fun isHope(rDate : String?): Boolean {
		if (sharedPref.getString(rDate, "") == "hope") {
			return true
		}
		return false
	}

	fun isJoy(rDate : String?): Boolean {
		if (sharedPref.getString(rDate, "") == "joy") {
			return true
		}
		return false
	}

	fun isNeutrality(rDate : String?): Boolean {
		if (sharedPref.getString(rDate, "") == "neutrality") {
			return true
		}
		return false
	}

	fun isRegret(rDate : String?): Boolean {
		if (sharedPref.getString(rDate, "") == "regret") {
			return true
		}
		return false
	}

	fun isSadness(rDate : String?): Boolean {
		if (sharedPref.getString(rDate, "") == "sadness") {
			return true
		}
		return false
	}

	fun isTiredness(rDate : String?): Boolean {
		if (sharedPref.getString(rDate, "") == "tiredness") {
			return true
		}
		return false
	}

	fun isError(rDate : String?): Boolean {
		if (sharedPref.getString(rDate, "") == "error") {
			return true
		}
		return false
	}
}