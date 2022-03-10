package com.nicer.attiary.view.signature

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.data.diary.DiaryList
import com.nicer.attiary.data.report.ReportDatabase
import com.nicer.attiary.databinding.ActivityHomeBinding
import com.nicer.attiary.view.write.WriteActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*

class HomeActivity : AppCompatActivity() {

	private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }

	var database: ReportDatabase? = null

	var startTimeCalendar = Calendar.getInstance()
	var endTimeCalendar = Calendar.getInstance()

	val currentYear = startTimeCalendar.get(Calendar.YEAR)
	val currentMonth = startTimeCalendar.get(Calendar.MONTH)
	val currentDate = startTimeCalendar.get(Calendar.DATE)
	val enCalendarDay = CalendarDay(endTimeCalendar.get(Calendar.YEAR),
		endTimeCalendar.get(Calendar.MONTH), endTimeCalendar.get(Calendar.DATE))

	val minMaxDecorator = MinMaxDecorator(enCalendarDay)

	//


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		binding.calendarView.addDecorators(minMaxDecorator)
		binding.toolbar.bringToFront()
		//set database
		database = ReportDatabase.getInstance(this)


		binding.calendarView.state().edit()
			.setFirstDayOfWeek(Calendar.MONDAY)
			.setMaximumDate(CalendarDay.from(currentYear, currentMonth, endTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)))
			.setCalendarDisplayMode(CalendarMode.MONTHS)
			.commit()
		binding.calendarView.isDynamicHeightEnabled = true

		binding.barFindText.text = monthToString(currentMonth)

		binding.calendarView.setOnMonthChangedListener { widget, date ->
			var year = date.year
			var month = date.month
			if (year == currentYear)
				binding.barFindText.text = monthToString(month)
			else
				binding.barFindText.text = "$year " + monthToString(month)
		}


		binding.newButton.setOnClickListener {
			var year = currentYear
			var month = currentMonth
			var dayOfMonth = currentDate
			var rDate = (year.toString() + month.toString() + dayOfMonth.toString()).toLong()

			nextView(year, month, dayOfMonth, rDate)
		}


		binding.calendarView.setOnDateChangedListener { widget, date, selected ->
			var year = widget.selectedDate.year
			var month = widget.selectedDate.month
			var dayOfMonth = widget.selectedDate.day
			var rDate = (year.toString() + month.toString() + dayOfMonth.toString()).toLong()
			nextView(year, month, dayOfMonth, rDate)

		}

		binding.statsView.setOnClickListener {
			startActivity(Intent(this, MonthlyReportActivity::class.java))
		}
	}

	fun monthToString(month: Int): String {
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

	fun nextView(year: Int, month: Int, dayOfMonth : Int, rDate: Long){
		if(DiaryList(this).isExist(rDate)){
			val intent: Intent = Intent(this, DiaryActivity::class.java)
			intent.putExtra("year", year)
			intent.putExtra("month", month)
			intent.putExtra("dayOfMonth", dayOfMonth)
			startActivity(intent)
		}
		else{
			val intent: Intent = Intent(this, WriteActivity::class.java)
			intent.putExtra("year", year);
			intent.putExtra("month", month);
			intent.putExtra("dayOfMonth", dayOfMonth);
			intent.putExtra("diary", "")
			startActivity(intent)
		}
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