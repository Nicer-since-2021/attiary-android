package com.nicer.attiary.view.signature

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.nicer.attiary.R
import com.nicer.attiary.data.password.AppLock
import com.nicer.attiary.databinding.ActivityMonthlyReportBinding
import com.nicer.attiary.view.common.AppPassWordActivity
import java.text.SimpleDateFormat
import java.util.*


class MonthlyReportActivity : AppCompatActivity() {

	private val binding by lazy { ActivityMonthlyReportBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		binding.toolbarBackBtn.setOnClickListener {
			finish()
		}

		makeEmotionChart()
		makeHappyChart()
		makeSadChart()
	}

	override fun onResume() {
		super.onResume()
		if (AppLock.AppLockStatus.lock && AppLock(this).isPassLockSet()) {
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra("type", AppLock.AppLockStatus.UNLOCK_PASSWORD)
			}
			startActivity(intent)
		}
		window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
	}

	override fun onPause() {
		super.onPause()
		window.setFlags(
			WindowManager.LayoutParams.FLAG_SECURE,
			WindowManager.LayoutParams.FLAG_SECURE
		)
	}

	private val ATTIARY_CHART_THEME = intArrayOf(
		Color.rgb(216, 216, 216), // 후회
		Color.rgb(252, 179, 169), // 분노
		Color.rgb(252, 209, 169), // 피곤
		Color.rgb(182, 219, 176), // 기쁨
		Color.rgb(169, 197, 252), // 슬픔
		Color.rgb(249, 198, 234), // 희망
		Color.rgb(190, 169, 252) // 불안
	)

	private fun makeEmotionChart() {
		with(binding) {
			pieChart.setUsePercentValues(true)
			pieChart.description.isEnabled = false
			pieChart.setExtraOffsets(10F, 15F, 10F, 15F)

			pieChart.dragDecelerationFrictionCoef = 0.95f

			pieChart.isDrawHoleEnabled = false
			pieChart.setHoleColor(Color.WHITE)
			pieChart.transparentCircleRadius = 61f

			val yValues = ArrayList<PieEntry>()

			yValues.add(PieEntry(34f, "후회"))
			yValues.add(PieEntry(23f, "분노"))
			yValues.add(PieEntry(14f, "피곤"))
			yValues.add(PieEntry(35f, "기쁨"))
			yValues.add(PieEntry(40f, "슬픔"))
			yValues.add(PieEntry(20f, "희망"))
			yValues.add(PieEntry(36f, "불안"))

			pieChart.animateY(1000, Easing.EaseInOutCubic)

			val dataSet = PieDataSet(yValues, "emotion")
			dataSet.sliceSpace = 0f
			dataSet.selectionShift = 0f
			dataSet.colors = ATTIARY_CHART_THEME.asList()

			val data = PieData(dataSet)

			// % 비율 숫자 관련
			data.setDrawValues(false)
			data.setValueTextSize(13f)
			data.setValueTextColor(Color.BLACK)

			// label 설정
			pieChart.setDrawEntryLabels(true)
			pieChart.setEntryLabelColor(Color.BLACK)
			pieChart.setEntryLabelTextSize(15f)
			pieChart.setEntryLabelTypeface(resources.getFont(R.font.im_heymin_regular))

			pieChart.setDrawMarkers(false)
			pieChart.description.isEnabled = false
			pieChart.legend.isEnabled = false
			pieChart.data = data
		}
	}

	private fun dateToMilliSecond(year: Int, month: Int, day: Int): Float {
		val calendar = Calendar.getInstance()
		calendar.set(year, month - 1, day)
		return calendar.timeInMillis.toFloat()
	}

	private fun makeHappyChart() {
		/**
		 * this is dummy data, get data from db
		 */
		val entries: MutableList<Entry> = ArrayList()
		entries.add(Entry(dateToMilliSecond(2022, 3, 1), 50.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 2), 90.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 3), 25.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 4), 37.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 6), 56.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 7), 66.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 8), 80.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 10), 20.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 11), 8.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 12), 27.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 16), 14.toFloat()))


		val lineDataSet = LineDataSet(entries, "행복 그래프")
		//        graph smoothing params
		lineDataSet.cubicIntensity = 0.5f
		lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
		lineDataSet.lineWidth = 2f
		lineDataSet.circleRadius = 5f
		lineDataSet.setCircleColor(Color.parseColor("#C0E1BE"))
		lineDataSet.color = Color.parseColor("#B6DBB0")
		lineDataSet.setDrawCircleHole(false)
		lineDataSet.setDrawCircles(true)
		lineDataSet.setDrawHorizontalHighlightIndicator(false)
		lineDataSet.setDrawHighlightIndicators(false)
		lineDataSet.setDrawValues(false)


		val lineData = LineData(lineDataSet)


		val lineChart: LineChart = binding.happyChart
		lineChart.data = lineData
		// remove line data set's label
		lineChart.legend.isEnabled = false
		// remove description
		lineChart.description.isEnabled = false
		lineChart.isDoubleTapToZoomEnabled = false
		lineChart.setDrawGridBackground(false)
		lineChart.animateY(2000, Easing.EaseInCubic)


		class DateAxisValueFormat : IndexAxisValueFormatter() {
			override fun getFormattedValue(value: Float): String {
				val date = Date(value.toLong())
				return SimpleDateFormat("MM/dd", Locale.KOREA).format(date)
			}
		}

		val xAxis: XAxis = lineChart.xAxis
		xAxis.position = XAxis.XAxisPosition.BOTTOM
		xAxis.textColor = Color.BLACK
		xAxis.valueFormatter = DateAxisValueFormat()
		xAxis.enableGridDashedLine(8f, 24f, 0f)

		val yLAxis: YAxis = lineChart.axisLeft
		yLAxis.textColor = Color.BLACK
		yLAxis.axisMaximum = 100F
		yLAxis.axisMinimum = 0F

		val yRAxis: YAxis = lineChart.axisRight
		yRAxis.setDrawLabels(false)
		yRAxis.setDrawAxisLine(false)
		yRAxis.setDrawGridLines(false)

		lineChart.invalidate()
	}

	private fun makeSadChart() {
		/**
		 * this is dummy data, get data from db
		 */
		val entries: MutableList<Entry> = ArrayList()
		entries.add(Entry(dateToMilliSecond(2022, 3, 1), 10.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 2), 5.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 3), 25.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 4), 37.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 6), 56.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 7), 66.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 8), 80.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 10), 60.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 11), 70.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 12), 50.toFloat()))
		entries.add(Entry(dateToMilliSecond(2022, 3, 16), 67.toFloat()))


		val lineDataSet = LineDataSet(entries, "우울 그래프")
		//        graph smoothing params
		lineDataSet.cubicIntensity = 0.5f
		lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
		lineDataSet.lineWidth = 2f
		lineDataSet.circleRadius = 5f
		lineDataSet.setCircleColor(Color.parseColor("#A6CEE3"))
		lineDataSet.color = Color.parseColor("#A9C5FC")
		lineDataSet.setDrawCircleHole(false)
		lineDataSet.setDrawCircles(true)
		lineDataSet.setDrawHorizontalHighlightIndicator(false)
		lineDataSet.setDrawHighlightIndicators(false)
		lineDataSet.setDrawValues(false)


		val lineData = LineData(lineDataSet)


		val lineChart: LineChart = binding.sadChart
		lineChart.data = lineData
		// remove line data set's label
		lineChart.legend.isEnabled = false
		// remove description
		lineChart.description.isEnabled = false
		lineChart.isDoubleTapToZoomEnabled = false
		lineChart.setDrawGridBackground(false)
		lineChart.animateY(2000, Easing.EaseInCubic)


		class DateAxisValueFormat : IndexAxisValueFormatter() {
			override fun getFormattedValue(value: Float): String {
				val date = Date(value.toLong())
				return SimpleDateFormat("MM/dd", Locale.KOREA).format(date)
			}
		}

		val xAxis: XAxis = lineChart.xAxis
		xAxis.position = XAxis.XAxisPosition.BOTTOM
		xAxis.textColor = Color.BLACK
		xAxis.valueFormatter = DateAxisValueFormat()
		xAxis.enableGridDashedLine(8f, 24f, 0f)

		val yLAxis: YAxis = lineChart.axisLeft
		yLAxis.textColor = Color.BLACK
		yLAxis.axisMaximum = 100F
		yLAxis.axisMinimum = 0F

		val yRAxis: YAxis = lineChart.axisRight
		yRAxis.setDrawLabels(false)
		yRAxis.setDrawAxisLine(false)
		yRAxis.setDrawGridLines(false)

		lineChart.invalidate()
	}
}