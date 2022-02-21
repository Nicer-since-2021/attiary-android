package com.nicer.attiary.view.signature

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.nicer.attiary.R
import com.nicer.attiary.databinding.FragmentMonthlyReportBinding


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
}