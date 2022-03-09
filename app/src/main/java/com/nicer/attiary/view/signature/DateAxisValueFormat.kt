package com.nicer.attiary.view.signature

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DateAxisValueFormat : IndexAxisValueFormatter() {

	override fun getFormattedValue(value: Float): String {
		val valueToDate = TimeUnit.DAYS.toDays(value.toLong())
		val date = Date(valueToDate)
		return SimpleDateFormat("yyyy/MM/dd", Locale.KOREA).format(date)
	}
}