package com.nicer.attiary.view.signature

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.nicer.attiary.R
import com.nicer.attiary.databinding.FragmentMonthlyReportBinding
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList


class MonthlyReportFragment : Fragment() {

	private val ATTIARY_CHART_THEME = intArrayOf(
		Color.rgb(216, 216, 216), // 후회
		Color.rgb(252, 179, 169), // 분노
		Color.rgb(252, 209, 169), // 피곤
		Color.rgb(182, 219, 176), // 기쁨
		Color.rgb(169, 197, 252), // 슬픔
		Color.rgb(249, 198, 234), // 희망
		Color.rgb(190, 169, 252) // 불안
	)

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding = FragmentMonthlyReportBinding.inflate(inflater, container, false)

		makeEmotionChart(binding)
		makeHappyChart(binding)
		return binding.root
	}

	private fun makeEmotionChart(binding: FragmentMonthlyReportBinding) {
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


	private fun makeHappyChart(binding: FragmentMonthlyReportBinding) {

//		val calendar = Calendar.getInstance()
//		val marchThird = calendar.set(2022, 3, 3)
//		var date: Date = Date(2022, 3, 3)
//		Log.d("[*] time", "$marchThird.timeInMillis")

		val entries: MutableList<Entry> = ArrayList()

		entries.add(Entry(1.toFloat(), 50.toFloat()))
		entries.add(Entry(3.toFloat(), 25.toFloat()))
		entries.add(Entry(5.toFloat(), 37.toFloat()))
		entries.add(Entry(8.toFloat(), 56.toFloat()))
		entries.add(Entry(11.toFloat(), 66.toFloat()))
		entries.add(Entry(13.toFloat(), 80.toFloat()))
		entries.add(Entry(16.toFloat(), 20.toFloat()))
		entries.add(Entry(17.toFloat(), 8.toFloat()))
		entries.add(Entry(21.toFloat(), 27.toFloat()))
		entries.add(Entry(27.toFloat(), 23.toFloat()))
		entries.add(Entry(30.toFloat(), 14.toFloat()))

		val lineDataSet = LineDataSet(entries, "")
		lineDataSet.lineWidth = 2f
		lineDataSet.circleRadius = 6f
		lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"))
//		lineDataSet.setCircleColorHole(Color.BLUE)
		lineDataSet.color = Color.parseColor("#FFA1B4DC")
		lineDataSet.setDrawCircleHole(false)
		lineDataSet.setDrawCircles(true)
		lineDataSet.setDrawHorizontalHighlightIndicator(false)
		lineDataSet.setDrawHighlightIndicators(false)
		lineDataSet.setDrawValues(false)
		lineDataSet.cubicIntensity = 1F

		val lineData = LineData(lineDataSet)

		val lineChart: LineChart = binding.happyChart
		lineChart.data = lineData
		lineChart.description.isEnabled = false

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

		val description = Description()
		description.isEnabled = false

		lineChart.isDoubleTapToZoomEnabled = false
		lineChart.setDrawGridBackground(false)
		lineChart.description = description
		lineChart.animateY(2000, Easing.EaseInCubic)
		lineChart.invalidate()
	}
}