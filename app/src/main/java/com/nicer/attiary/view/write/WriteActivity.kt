package com.nicer.attiary.view.write

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.data.report.Report
import com.nicer.attiary.data.report.ReportDatabase
import com.nicer.attiary.databinding.ActivityWriteBinding
import com.nicer.attiary.view.signature.DiaryActivity
import com.nicer.attiary.view.signature.userID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileOutputStream

class WriteActivity : AppCompatActivity() {
	val binding by lazy { ActivityWriteBinding.inflate(layoutInflater) }

	lateinit var fname: String
	lateinit var str: String
	private var database: ReportDatabase? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		database = ReportDatabase.getInstance(this)
		setContentView(binding.root)
		val intent: Intent = getIntent()
		var year = intent.getIntExtra("year", 0)
		var month = intent.getIntExtra("month", 0)
		var dayOfMonth = intent.getIntExtra("dayOfMonth", 0)
		str = intent.getStringExtra("diary").toString()
		var rDate = (year.toString()+month.toString()+dayOfMonth.toString()).toLong()

		binding.backBtn.setOnClickListener {
			finish()
		}

		binding.diaryTextView.text = String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth)
		binding.contextEditText.setText(str)




		binding.saveBtn.setOnClickListener {
			if(binding.contextEditText.text.isBlank()){
				val builder = AlertDialog.Builder(this)
				builder.setMessage("내용을 입력하세요.")
				builder.setPositiveButton("확인",null)
				builder.show()
			}
			else{
				//저장
				fname = "" + userID + year + "-" + (month + 1) + "" + "-" + dayOfMonth + ".txt"
				try {
					var fileInputStream: FileInputStream
					fileInputStream = openFileInput(fname)
					val fileData = ByteArray(fileInputStream.available())
					fileInputStream.read(fileData)
					fileInputStream.close()
					val str = String(fileData)
					if(str != ""){
						CoroutineScope(Dispatchers.IO).launch {
							database?.ReportDao()?.delete(rDate)
						}
					}
				}catch (e: Exception) {
				}
				saveDiary(fname)
				CoroutineScope(Dispatchers.IO).launch {
					database?.ReportDao()?.insert(
						Report(rDate, "a", 50, "s", 30, "ax", 20, "ha", 10)
					)
				}

				val intent: Intent = Intent(this, DiaryActivity::class.java)
				intent.putExtra("year", year)
				intent.putExtra("month", month)
				intent.putExtra("dayOfMonth", dayOfMonth)
				startActivity(intent)
				finish()
			}

		}


	}




	// 달력 내용 제거



	// 달력 내용 추가
	@SuppressLint("WrongConstant")
	fun saveDiary(readDay: String?) {
		var fileOutputStream: FileOutputStream
		try {
			fileOutputStream = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
			val content = binding.contextEditText.text.toString()
			fileOutputStream.write(content.toByteArray())
			fileOutputStream.close()
		} catch (e: java.lang.Exception) {
			e.printStackTrace()
		}
	}

//	private fun saveReport(date: Long): Report {
//		//test code
//		val report =
//			Report.Builder(date)
//				.firstEmotion("a")
//				.firstPercent(90)
//				.secondEmotion("s")
//				.secondPercent(30)
//				.thirdEmotion("ax")
//				.thirdPercent(20)
//				.fourthEmotion("ha")
//				.fourthPercent(10)
//				.build()
//		database?.ReportDao()?.insert(report)
//
//		return report
//	}

	override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
		val focusView = currentFocus
		if (focusView != null && ev != null) {
			val rect = Rect()
			focusView.getGlobalVisibleRect(rect)
			val x = ev.x.toInt()
			val y = ev.y.toInt()

			if (!rect.contains(x, y)) {
				val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
				imm?.hideSoftInputFromWindow(focusView.windowToken, 0)
				focusView.clearFocus()
			}
		}
		return super.dispatchTouchEvent(ev)
	}


}