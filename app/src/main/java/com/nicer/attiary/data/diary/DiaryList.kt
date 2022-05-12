package com.nicer.attiary.data.diary

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prolificinteractive.materialcalendarview.CalendarDay


class DiaryList(context: Context) {
	private var sharedPref = context.getSharedPreferences("Diary", Context.MODE_PRIVATE)
	// 목록에 일기 추가
	fun addDiary(rDate : CalendarDay, emotion : String) {
		sharedPref.edit().apply {
			putString(rDate.toString(), emotion)
			apply()
		}
	}

	// 목록에서 해당 날짜 일기 제거
	fun removeDiary(rDate : CalendarDay) {
		sharedPref.edit().apply {
			remove(rDate.toString())
			apply()
		}
	}

	fun isExist(rDate : CalendarDay?): Boolean {
		if (sharedPref.contains(rDate.toString())) {
			return true
		}
		return false
	}

	fun isAnger(rDate : CalendarDay?): Boolean {
		if (sharedPref.getString(rDate.toString(), "") == "anger") {
			return true
		}
		return false
	}

	fun isAnxiety(rDate : CalendarDay?): Boolean {
		if (sharedPref.getString(rDate.toString(), "") == "anxiety") {
			return true
		}
		return false
	}

	fun isHope(rDate : CalendarDay?): Boolean {
		if (sharedPref.getString(rDate.toString(), "") == "hope") {
			return true
		}
		return false
	}

	fun isJoy(rDate : CalendarDay?): Boolean {
		if (sharedPref.getString(rDate.toString(), "") == "joy") {
			return true
		}
		return false
	}

	fun isNeutrality(rDate : CalendarDay?): Boolean {
		if (sharedPref.getString(rDate.toString(), "") == "neutrality") {
			return true
		}
		return false
	}

	fun isRegret(rDate : CalendarDay?): Boolean {
		if (sharedPref.getString(rDate.toString(), "") == "regret") {
			return true
		}
		return false
	}

	fun isSadness(rDate : CalendarDay?): Boolean {
		if (sharedPref.getString(rDate.toString(), "") == "sadness") {
			return true
		}
		return false
	}

	fun isTiredness(rDate : CalendarDay?): Boolean {
		if (sharedPref.getString(rDate.toString(), "") == "tiredness") {
			return true
		}
		return false
	}

	fun isError(rDate : CalendarDay?): Boolean {
		if (sharedPref.getString(rDate.toString(), "") == "error") {
			return true
		}
		return false
	}

}