package com.nicer.attiary.view.signature

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.data.diary.DiaryList
import com.nicer.attiary.data.password.AppLock
import com.nicer.attiary.data.report.ReportDatabase
import com.nicer.attiary.databinding.ActivityHomeBinding
import com.nicer.attiary.view.common.AppPassWordActivity
import com.nicer.attiary.view.setting.lock.SettingActivity
import com.nicer.attiary.view.write.WriteActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*


class HomeActivity : AppCompatActivity() {

	private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

	var database: ReportDatabase? = null
	private var startTimeCalendar = Calendar.getInstance()
	private var endTimeCalendar = Calendar.getInstance()
	private val currentYear = startTimeCalendar.get(Calendar.YEAR)
	private val currentMonth = startTimeCalendar.get(Calendar.MONTH)
	private val currentDate = startTimeCalendar.get(Calendar.DATE)
	private val enCalendarDay = CalendarDay(
		endTimeCalendar.get(Calendar.YEAR),
		endTimeCalendar.get(Calendar.MONTH),
		endTimeCalendar.get(Calendar.DATE)
	)
	private val minMaxDecorator = MinMaxDecorator(enCalendarDay)
	lateinit var intent_music: Intent

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		intent_music = Intent(this, MusicService::class.java)

		binding.calendarView.addDecorators(minMaxDecorator)
		binding.toolbar.bringToFront()
		database = ReportDatabase.getInstance(this)
		binding.calendarView.state().edit()
			.setFirstDayOfWeek(Calendar.MONDAY)
			.setMaximumDate(
				CalendarDay.from(
					currentYear,
					currentMonth,
					endTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
				)
			)
			.setCalendarDisplayMode(CalendarMode.MONTHS)
			.commit()
		binding.calendarView.isDynamicHeightEnabled = true
		binding.barFindText.text = monthToString(currentMonth)
		binding.calendarView.setOnMonthChangedListener { _, date ->
			val year = date.year
			val month = date.month
			if (year == currentYear)
				binding.barFindText.text = monthToString(month)
			else
				("$year " + monthToString(month)).also { binding.barFindText.text = it }
		}

		binding.newButton.setOnClickListener {
			val year = currentYear
			val month = currentMonth
			val dayOfMonth = currentDate
			val rDate = (year.toString() + month.toString() + dayOfMonth.toString()).toLong()

			nextView(year, month, dayOfMonth, rDate)
		}

		binding.calendarView.setOnDateChangedListener { widget, date, selected ->
			val year = widget.selectedDate.year
			val month = widget.selectedDate.month
			val dayOfMonth = widget.selectedDate.day
			val rDate = (year.toString() + month.toString() + dayOfMonth.toString()).toLong()
			nextView(year, month, dayOfMonth, rDate)
		}


		binding.settingBtn.setOnClickListener{
			startActivity(Intent(this, SettingActivity::class.java))
		}

		binding.statsView.setOnClickListener {
			startActivity(
				Intent(
					this,
					MonthlyReportActivity::class.java
				).apply { addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION) })
		}
	}

	private fun monthToString(month: Int): String {
		lateinit var monthString: String
		when (month) {
			0 -> monthString = "January"
			1 -> monthString = "February"
			2 -> monthString = "March"
			3 -> monthString = "April"
			4 -> monthString = "May"
			5 -> monthString = "June"
			6 -> monthString = "July"
			7 -> monthString = "August"
			8 -> monthString = "September"
			9 -> monthString = "October"
			10 -> monthString = "November"
			11 -> monthString = "December"
		}
		return monthString
	}

	fun nextView(year: Int, month: Int, dayOfMonth: Int, rDate: Long) {
		if (DiaryList(this).isExist(rDate)) {
			val intent: Intent = Intent(this, DiaryActivity::class.java)
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
			intent.putExtra("year", year)
			intent.putExtra("month", month)
			intent.putExtra("dayOfMonth", dayOfMonth)
			startActivity(intent)
		} else {
			val intent: Intent = Intent(this, WriteActivity::class.java)
			intent.putExtra("year", year);
			intent.putExtra("month", month);
			intent.putExtra("dayOfMonth", dayOfMonth);
			intent.putExtra("diary", "")
			startActivity(intent)
		}
	}

	override fun onResume() {
		super.onResume()
		if (AppLock.AppLockStatus.lock && AppLock(this).isPassLockSet()) {
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION)
				putExtra("type", AppLock.AppLockStatus.UNLOCK_PASSWORD)
			}
			startActivity(intent)
		}
		window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
		startService(intent_music)
	}

	override fun onUserLeaveHint() {
		super.onUserLeaveHint()
		stopService(intent_music)
	}

	override fun onPause() {
		super.onPause()
		window.setFlags(
			WindowManager.LayoutParams.FLAG_SECURE,
			WindowManager.LayoutParams.FLAG_SECURE
		)
	}
}

class MinMaxDecorator(max: CalendarDay) : DayViewDecorator {
	val maxDay = max
	override fun shouldDecorate(day: CalendarDay?): Boolean {
		return (day?.month == maxDay.month && day.day > maxDay.day)
	}

	override fun decorate(view: DayViewFacade?) {
		view?.addSpan(object : ForegroundColorSpan(Color.parseColor("#E5E4E4")) {})
		view?.setDaysDisabled(true)
	}
}