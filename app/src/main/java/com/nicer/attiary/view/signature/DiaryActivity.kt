package com.nicer.attiary.view.signature


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.R
import com.nicer.attiary.data.diary.DiaryList
import com.nicer.attiary.data.report.ReportDatabase
import com.nicer.attiary.databinding.ActivityDiaryBinding
import com.nicer.attiary.view.write.WriteActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryActivity : AppCompatActivity() {
	var str: String = ""
	var database: ReportDatabase? = null

	private val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		//set layout
		binding.diaryContent.bringToFront()

		//set database
		database = ReportDatabase.getInstance(this)


		//HomeAvtivity로부터 받은 날짜 data
		val intent: Intent = getIntent()
		var year = intent.getIntExtra("year", 0)
		var month = intent.getIntExtra("month", 0)
		var dayOfMonth = intent.getIntExtra("dayOfMonth", 0)

		//해당 날짜 표시
		binding.diaryTextView.text = String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth)

		//일기 상태 확인
		checkDay(year, month, dayOfMonth)

		binding.buttonBack.setOnClickListener {
			finish()
		}

		/** database 접근 -> report 표시 **/

		var rDate = (year.toString() + month.toString() + dayOfMonth.toString()).toLong()
		CoroutineScope(Dispatchers.IO).launch {
			//날짜로 접근
			var report = database?.ReportDao()?.findByDate(rDate)

			CoroutineScope(Dispatchers.Main).launch {
				//현재는 테스트 데이터로 통일되어 있음.
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
	fun checkDay(cYear: Int, cMonth: Int, cDay: Int) {

		var rDate = (cYear.toString() + cMonth.toString() + cDay.toString()).toLong()
		CoroutineScope(Dispatchers.IO).launch {
			//날짜로 접근
			var report = database?.ReportDao()?.findByDate(rDate)
			CoroutineScope(Dispatchers.Main).launch {
				str = report?.diaryContent.toString()
				binding.diaryContent.text = str
			}

		}



		binding.updateBtn.setOnClickListener {
			val intent: Intent = Intent(this, WriteActivity::class.java)
			intent.putExtra("year", cYear)
			intent.putExtra("month", cMonth)
			intent.putExtra("dayOfMonth", cDay)
			intent.putExtra("diary", str)
			startActivity(intent)
			finish()
		}
		binding.deleteBtn.setOnClickListener {
			val builder = AlertDialog.Builder(this)
			builder.setMessage("정말 삭제하시겠습니까?")
			builder.setNegativeButton("취소", null)
			builder.setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
				DiaryList(this).removeDiary(rDate)
				CoroutineScope(Dispatchers.IO).launch {
					database?.ReportDao()?.delete(rDate)
					finish()
				}
			})
			builder.show()
		}

	}

}