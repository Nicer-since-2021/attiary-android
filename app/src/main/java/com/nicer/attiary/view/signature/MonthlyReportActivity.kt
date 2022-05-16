package com.nicer.attiary.view.signature

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.gson.Gson
import com.nicer.attiary.R
import com.nicer.attiary.data.password.AppLock
import com.nicer.attiary.data.report.ReportDatabase
import com.nicer.attiary.databinding.ActivityMonthlyReportBinding
import com.nicer.attiary.util.Emotion
import com.nicer.attiary.view.common.AppPassWordActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class MonthlyReportActivity : AppCompatActivity() {

    var database: ReportDatabase? = null
    private val binding by lazy { ActivityMonthlyReportBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = ReportDatabase.getInstance(this, Gson())

        binding.toolbarBackBtn.setOnClickListener {
            finish()
        }

        makeEmotionChart()
        makeHappyChart()
        makeSadChart()
        bindTop3Diary()
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

    private fun makeEmotionChart() {
        with(binding) {
            CoroutineScope(Dispatchers.IO).launch {
                val emotionList: List<Emotion> =
                    listOf(
                        Emotion.REGRET,
                        Emotion.ANGER,
                        Emotion.TIREDNESS,
                        Emotion.JOY,
                        Emotion.SADNESS,
                        Emotion.ANXIETY
                    )
                val reports = database?.ReportDao()?.findTop10(10)!!

                // 1. map으로 각 최고 감정의 개수 세기
                val map: HashMap<String, Float> = HashMap()
                for (report in reports) {
                    for (emotion in emotionList) {
                        val reportEmotion =
                            report.representative.substring(0, report.representative.length - 1)
                        Log.d("파이차트 데이터", reportEmotion)
                        if (reportEmotion == emotion.en) {
                            val cnt = map[emotion.en]
                            if (cnt == null) {
                                map[emotion.en] = 1F
                            } else {
                                map[emotion.en] = cnt + 1F
                            }
                        }
                    }
                }
                for (m in map) {
                    Log.d("파이차트 데이터", "${m.key}, ${m.value}")
                }

                // 2. 그래프 데이터와 그래프 색 지정
                val yValues = ArrayList<PieEntry>()
                val colors: MutableList<Int> = ArrayList<Int>()
                for (emotion in emotionList) {
                    if (map.containsKey(emotion.en)) {
                        yValues.add(PieEntry(map[emotion.en]!!, emotion.kr))
                        colors.add(emotion.color)
                    }
                }

                // 3. 그래프 그리기
                pieChart.setUsePercentValues(true)
                pieChart.description.isEnabled = false
                pieChart.setExtraOffsets(10F, 15F, 10F, 15F)

                pieChart.dragDecelerationFrictionCoef = 0.95f

                pieChart.isDrawHoleEnabled = false
                pieChart.setHoleColor(Color.WHITE)
                pieChart.transparentCircleRadius = 61f

                // pieChart.animateY(1000, Easing.EaseInOutCubic)

                val dataSet = PieDataSet(yValues, "emotion")
                dataSet.sliceSpace = 0f
                dataSet.selectionShift = 0f
                dataSet.colors = colors

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

    private fun dateToMilliSecond(year: Int, month: Int, day: Int): Float {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day)
        return calendar.timeInMillis.toFloat()
    }

    private fun makeHappyChart() {

        // read data, add entries
        CoroutineScope(Dispatchers.IO).launch {
            val entries: MutableList<Entry> = ArrayList()
            val reports = database?.ReportDao()?.findTop10(10)!!
            reports.reverse()
            for (report in reports) {
                Log.d("행복지수 불러오기", "date: ${report.rDate}, happiness: ${report.happiness}")
                val y = report.rDate?.substring(0 until 4)?.toInt()
                val m = report.rDate?.substring(4 until 6)?.toInt()
                val d = report.rDate?.substring(6 until 8)?.toInt()
                entries.add(Entry(dateToMilliSecond(y!!, m!!, d!!), report.happiness.toFloat()))
            }

            // draw graph
            CoroutineScope(Dispatchers.IO).launch {

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
                // lineChart.animateY(2000, Easing.EaseInCubic)


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
    }


    private fun makeSadChart() {
        // read data, add entires
        CoroutineScope(Dispatchers.IO).launch {
            val entries: MutableList<Entry> = ArrayList()
            val reports = database?.ReportDao()?.findTop10(10)!!
            reports.reverse()
            for (report in reports) {
                Log.d("우울지수 불러오기", "date: ${report.rDate}, depression: ${report.depression}")
                val y = report.rDate?.substring(0 until 4)?.toInt()
                val m = report.rDate?.substring(4 until 6)?.toInt()
                val d = report.rDate?.substring(6 until 8)?.toInt()
                entries.add(Entry(dateToMilliSecond(y!!, m!!, d!!), report.depression.toFloat()))
            }

            CoroutineScope(Dispatchers.IO).launch {
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
                // lineChart.animateY(2000, Easing.EaseInCubic)


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
    }


    private fun bindTop3Diary() {
        CoroutineScope(Dispatchers.IO).launch {
            val reports = database?.ReportDao()?.findHappinessTop3()!!
            binding.diaryTop1Btn.setOnClickListener {
                if (reports.isEmpty()) {
                    val builder = AlertDialog.Builder(this@MonthlyReportActivity)
                    builder.setMessage("작성된 일기가 없습니다.")
                    builder.setPositiveButton("확인") { _, _ ->
                    }
                    builder.show()
                } else {
                    val y = reports[0].rDate.substring(0 until 4).toInt()
                    val m = reports[0].rDate.substring(4 until 6).toInt()
                    val d = reports[0].rDate.substring(6 until 8).toInt()
                    val intent = Intent(this@MonthlyReportActivity, DiaryActivity::class.java)
                    intent.putExtra("year", y)
                    intent.putExtra("month", m - 1)
                    intent.putExtra("dayOfMonth", d)
                    startActivity(intent)
                }
            }
            binding.diaryTop2Btn.setOnClickListener {
                if (reports.size < 2) {
                    val builder = AlertDialog.Builder(this@MonthlyReportActivity)
                    builder.setMessage("작성된 일기가 없습니다.")
                    builder.setPositiveButton("확인") { _, _ ->
                    }
                    builder.show()
                } else {
                    val y = reports[1].rDate.substring(0 until 4).toInt()
                    val m = reports[1].rDate.substring(4 until 6).toInt()
                    val d = reports[1].rDate.substring(6 until 8).toInt()
                    val intent = Intent(this@MonthlyReportActivity, DiaryActivity::class.java)
                    intent.putExtra("year", y)
                    intent.putExtra("month", m - 1)
                    intent.putExtra("dayOfMonth", d)
                    startActivity(intent)
                }
            }
            binding.diaryTop3Btn.setOnClickListener {
                if (reports.size < 3) {
                    val builder = AlertDialog.Builder(this@MonthlyReportActivity)
                    builder.setMessage("작성된 일기가 없습니다.")
                    builder.setPositiveButton("확인") { _, _ ->
                    }
                    builder.show()
                } else {
                    val y = reports[2].rDate.substring(0 until 4).toInt()
                    val m = reports[2].rDate.substring(4 until 6).toInt()
                    val d = reports[2].rDate.substring(6 until 8).toInt()
                    val intent = Intent(this@MonthlyReportActivity, DiaryActivity::class.java)
                    intent.putExtra("year", y)
                    intent.putExtra("month", m - 1)
                    intent.putExtra("dayOfMonth", d)
                    startActivity(intent)
                }
            }
        }
    }
}