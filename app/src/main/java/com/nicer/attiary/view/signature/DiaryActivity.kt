package com.nicer.attiary.view.signature

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.R
import com.nicer.attiary.data.report.ReportDatabase
import com.nicer.attiary.databinding.ActivityDiaryBinding
import com.nicer.attiary.view.write.WriteActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DiaryActivity : AppCompatActivity() {
	lateinit var fname: String
	lateinit var str: String
	private var database: ReportDatabase? = null

	private val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		database = ReportDatabase.getInstance(this)
		binding.diaryContent.bringToFront()
		val intent: Intent = getIntent()
		var year = intent.getIntExtra("year", 0)
		var month = intent.getIntExtra("month", 0)
		var dayOfMonth = intent.getIntExtra("dayOfMonth", 0)
		binding.diaryTextView.text = String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth)
		checkDay(year, month, dayOfMonth, userID)
		binding.buttonBack.setOnClickListener {
			finish()
		}
		var rDate = (year.toString() + month.toString() + dayOfMonth.toString()).toLong()
		CoroutineScope(Dispatchers.IO).launch {
			var report = database?.ReportDao()?.findByDate(rDate)
			CoroutineScope(Dispatchers.Main).launch {
				var e1 = report?.firstEmotion
				var p1 = report?.firstPercent
				CoroutineScope(Dispatchers.Main).launch {
					when (e1) {
						"a" -> {
							binding.txtEmotion1.text = "화가 나요"
							binding.Index1.progressDrawable = getDrawable(R.drawable.progress_anger)
						}
					}
					binding.percent1.text = p1.toString() + "%"
					if (p1 != null) {
						binding.Index1.progress = p1
					}
				}

			}
		}
	}

	// 달력 내용 조회, 수정
	fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userID: String) {
		//저장할 파일 이름설정
		fname = "" + userID + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"
		var rDate = (cYear.toString() + cMonth.toString() + cDay.toString()).toLong()

		var fileInputStream: FileInputStream
		try {
			fileInputStream = openFileInput(fname)
			val fileData = ByteArray(fileInputStream.available())
			fileInputStream.read(fileData)
			fileInputStream.close()
			str = String(fileData)
			binding.diaryContent.text = str
			binding.updateBtn.setOnClickListener {
				val intent: Intent = Intent(this, WriteActivity::class.java)
				intent.putExtra("year", cYear)
				intent.putExtra("month", cMonth)
				intent.putExtra("dayOfMonth", cDay)
				intent.putExtra("diary", str)
				startActivity(intent)
			}
			binding.deleteBtn.setOnClickListener {
				val builder = AlertDialog.Builder(this)
				builder.setMessage("정말 삭제하시겠습니까?")
				builder.setNegativeButton("취소", null)
				builder.setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
					removeDiary(fname)
					var file = File(fname)
					file.delete()
					CoroutineScope(Dispatchers.IO).launch {
						database?.ReportDao()?.delete(rDate)
					}
					finish()
				})
				builder.show()
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	@SuppressLint("WrongConstant")
	fun removeDiary(readDay: String?) {
		var fileOutputStream: FileOutputStream
		try {
			fileOutputStream = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
			val content = ""
			fileOutputStream.write(content.toByteArray())
			fileOutputStream.close()
		} catch (e: java.lang.Exception) {
			e.printStackTrace()
		}
	}

}