package com.nicer.attiary.view.signature


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.R
import com.nicer.attiary.data.diary.DiaryList
import com.nicer.attiary.data.password.AppLock
import com.nicer.attiary.data.report.ReportDatabase
import com.nicer.attiary.databinding.ActivityDiaryBinding
import com.nicer.attiary.view.setting.lock.AppPassWordActivity
import com.nicer.attiary.view.write.WriteActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryActivity : AppCompatActivity() {
	var str: String = ""
	var database: ReportDatabase? = null
	lateinit var intent_music: Intent

	private val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		intent_music = Intent(this, MusicService::class.java)
		val intent: Intent = getIntent()
		val year = intent.getIntExtra("year", 0)
		val month = intent.getIntExtra("month", 0)
		val dayOfMonth = intent.getIntExtra("dayOfMonth", 0)
		database = ReportDatabase.getInstance(this)

		binding.diaryContent.bringToFront()
		binding.diaryTextView.text = String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth)

		processDiary(year, month, dayOfMonth)
		processReport(year, month, dayOfMonth)

		binding.buttonBack.setOnClickListener {
			finish()
		}
	}

	private fun processDiary(cYear: Int, cMonth: Int, cDay: Int) {

		val rDate = (cYear.toString() + cMonth.toString() + cDay.toString()).toLong()
		CoroutineScope(Dispatchers.IO).launch {
			val report = database?.ReportDao()?.findByDate(rDate)
			CoroutineScope(Dispatchers.Main).launch {
				str = report?.diaryContent.toString()
				binding.diaryContent.text = str
			}
		}

		binding.updateBtn.setOnClickListener {
			stopService(intent_music)
			val intent = Intent(this, WriteActivity::class.java)
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
			builder.setPositiveButton("확인") { _, i ->
				DiaryList(this).removeDiary(rDate)
				CoroutineScope(Dispatchers.IO).launch {
					database?.ReportDao()?.delete(rDate)
					finish()
				}
			}
			builder.show()
		}
	}

	private fun processReport(cYear: Int, cMonth: Int, cDay: Int) {
		val rDate = (cYear.toString() + cMonth.toString() + cDay.toString()).toLong()
		CoroutineScope(Dispatchers.IO).launch {
			val report = database?.ReportDao()?.findByDate(rDate)

			CoroutineScope(Dispatchers.Main).launch {
				//현재는 테스트 데이터로 통일되어 있음.
				val e1 = report?.firstEmotion
				val e2 = report?.secondEmotion
				val e3 = report?.thirdEmotion
				val e4 = report?.fourthEmotion
				val p1 = report?.firstPercent
				val p2 = report?.secondPercent
				val p3 = report?.thirdPercent
				val p4 = report?.fourthPercent
				processEmotion(1, e1, p1)
				processEmotion(2, e2, p2)
				processEmotion(3, e3, p3)
				processEmotion(4, e4, p4)
				val comment = report?.commentFromAtti
				binding.commentFromAtti.text = comment

			}
		}
	}

	@SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
	fun processEmotion(rank: Int, emotion: String?, percent: Int?) {
		lateinit var txtEmotion: TextView
		lateinit var bar: ProgressBar
		lateinit var txtPercent: TextView

		when (rank) {
			1 -> {
				txtEmotion = binding.txtEmotion1
				bar = binding.Index1
				txtPercent = binding.percent1
			}
			2 -> {
				txtEmotion = binding.txtEmotion2
				bar = binding.Index2
				txtPercent = binding.percent2
			}
			3 -> {
				txtEmotion = binding.txtEmotion3
				bar = binding.Index3
				txtPercent = binding.percent3
			}
			4 -> {
				txtEmotion = binding.txtEmotion4
				bar = binding.Index4
				txtPercent = binding.percent4
			}


		}

		when (emotion) {
			"a" -> {
				txtEmotion.text = getString(R.string.anger)
				bar.progressDrawable = getDrawable(R.drawable.progress_anger)
			}
			"s" -> {
				txtEmotion.text = getString(R.string.sadness)
				bar.progressDrawable = getDrawable(R.drawable.progress_sadness)
			}
			"ax" -> {
				txtEmotion.text = getString(R.string.anxiety)
				bar.progressDrawable = getDrawable(R.drawable.progress_anxiety)
			}
			"t" -> {
				txtEmotion.text = getString(R.string.tiredness)
				bar.progressDrawable = getDrawable(R.drawable.progress_tiredness)
			}
			"r" -> {
				txtEmotion.text = getString(R.string.regret)
				bar.progressDrawable = getDrawable(R.drawable.progress_regret)
			}
			"ha" -> {
				txtEmotion.text = getString(R.string.happiness)
				bar.progressDrawable = getDrawable(R.drawable.progress_happiness_diary)
			}
			"ho" -> {
				txtEmotion.text = getString(R.string.hope)
				bar.progressDrawable = getDrawable(R.drawable.progress_hope)
			}

		}

		(percent.toString() + "%").also { txtPercent.text = it }
		if (percent != null) bar.progress = percent

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

}