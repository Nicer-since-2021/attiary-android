package com.nicer.attiary.view.signature


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.text.method.ScrollingMovementMethod
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.gson.Gson
import com.nicer.attiary.R
import com.nicer.attiary.data.diary.DiaryList
import com.nicer.attiary.data.password.AppLock
import com.nicer.attiary.data.report.ReportDatabase
import com.nicer.attiary.databinding.ActivityDiaryBinding
import com.nicer.attiary.util.RDate
import com.nicer.attiary.view.common.AppPassWordActivity
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

		val intent: Intent = getIntent()
		val year = intent.getIntExtra("year", 0)
		val month = intent.getIntExtra("month", 0)
		val dayOfMonth = intent.getIntExtra("dayOfMonth", 0)

		val data = intent.getIntExtra("data", 1)

		if (data == 0){
			binding.errorFragment.bringToFront()
			setErrorFrag()
		}

		database = ReportDatabase.getInstance(this, Gson())
		binding.diaryContent.setMovementMethod(ScrollingMovementMethod())


		binding.diaryContent.bringToFront()
		binding.diaryTextView.text = String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth)

		processDiary(year, month, dayOfMonth)
		processReport(year, month, dayOfMonth)

		binding.buttonBack.setOnClickListener {
			finish()
		}
		binding.errorFragment.bringToFront()

		binding.emotion.setOnClickListener{
			val rDate = RDate.toRDate(year,month,dayOfMonth)
			binding.setEmotionFrag.bringToFront()
			setEmotionFragment()
			supportFragmentManager
				.setFragmentResultListener("requestKey", this) { requestKey, bundle ->
					val result = bundle.getString("bundleKey")
					processEmoji(result!!)
					CoroutineScope(Dispatchers.IO).launch {
						database?.ReportDao()?.updateRepresentative(result!!, rDate)
					}
					updateDiaryList(result!!, rDate, "anger")
					updateDiaryList(result!!, rDate, "anxiety")
					updateDiaryList(result!!, rDate, "hope")
					updateDiaryList(result!!, rDate, "joy")
					updateDiaryList(result!!, rDate, "neutrality")
					updateDiaryList(result!!, rDate, "regret")
					updateDiaryList(result!!, rDate, "sadness")
					updateDiaryList(result!!, rDate, "tiredness")
				}

		}

	}

	private fun setErrorFrag() {
		val transaction = supportFragmentManager.beginTransaction()
		transaction
			.replace(R.id.wholeView, ErrorFragment())
			.addToBackStack(null)
			.commit()
	}

	private fun setEmotionFragment() {
		val transaction = supportFragmentManager.beginTransaction()
		transaction
			.replace(R.id.setEmotionFrag, SetEmotionFragment())
			.addToBackStack(null)
			.commit()
	}

	private fun updateDiaryList(result: String, rDate: String, emotion: String) {
		if (result.contains(emotion)) {
			DiaryList(this).removeDiary(rDate)
			DiaryList(this).addDiary(rDate, emotion)
		}
	}


	private fun processDiary(cYear: Int, cMonth: Int, cDay: Int) {

		val rDate = RDate.toRDate(cYear, cMonth, cDay)
		CoroutineScope(Dispatchers.IO).launch {
			val report = database?.ReportDao()?.findByDate(rDate)
			CoroutineScope(Dispatchers.Main).launch {
				Log.d("diary_view", "${report?.reportId}")
				str = report?.diaryContent.toString()
				binding.diaryContent.text = str
			}
		}

		binding.updateBtn.setOnClickListener {
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
				DiaryList(this).removeDiary(RDate.toRDate(cYear, cMonth, cDay))
				CoroutineScope(Dispatchers.IO).launch {
					database?.ReportDao()?.delete(rDate)
					finish()
				}
			}
			builder.show()
		}
	}

	private fun processReport(cYear: Int, cMonth: Int, cDay: Int) {
		val rDate = RDate.toRDate(cYear, cMonth, cDay)
		CoroutineScope(Dispatchers.IO).launch {
			val report = database?.ReportDao()?.findByDate(rDate)
			CoroutineScope(Dispatchers.Main).launch {
				//현재는 테스트 데이터로 통일되어 있음.
				val representative = report?.representative.toString()
				processEmoji(representative)
				val emotions2 = report?.emotions?.toList()?.sortedByDescending{ (_, value) -> value}?.toMap() as HashMap<String, Int>
				val e1 = emotions2.keys.elementAt(0)
				val e2 = emotions2.keys.elementAt(1)
				val e3 = emotions2.keys.elementAt(2)
				val e4 = emotions2.keys.elementAt(3)
				val p1 = emotions2.getValue(e1)
				val p2 = emotions2.getValue(e2)
				val p3 = emotions2.getValue(e3)
				val p4 = emotions2.getValue(e4)
				processEmotion(1, e1, p1)
				processEmotion(2, e2, p2)
				processEmotion(3, e3, p3)
				processEmotion(4, e4, p4)
				val comment = report?.commentFromAtti
				binding.commentFromAtti.text = comment
			}
		}
	}

	fun processEmoji(representative: String){
		when(representative){
			"anger1" -> binding.emotion.setImageResource(R.drawable.emotion_anger1)
			"anger2" -> binding.emotion.setImageResource(R.drawable.emotion_anger2)
			"anger3" -> binding.emotion.setImageResource(R.drawable.emotion_anger3)
			"anxiety1" -> binding.emotion.setImageResource(R.drawable.emotion_anxiety1)
			"anxiety2" -> binding.emotion.setImageResource(R.drawable.emotion_anxiety2)
			"anxiety3" -> binding.emotion.setImageResource(R.drawable.emotion_anxiety3)
			"hope1" -> binding.emotion.setImageResource(R.drawable.emotion_hope1)
			"hope2" -> binding.emotion.setImageResource(R.drawable.emotion_hope2)
			"hope3" -> binding.emotion.setImageResource(R.drawable.emotion_hope3)
			"joy1" -> binding.emotion.setImageResource(R.drawable.emotion_joy1)
			"joy2" -> binding.emotion.setImageResource(R.drawable.emotion_joy2)
			"joy3" -> binding.emotion.setImageResource(R.drawable.emotion_joy3)
			"regret1" -> binding.emotion.setImageResource(R.drawable.emotion_regret1)
			"regret2" -> binding.emotion.setImageResource(R.drawable.emotion_regret2)
			"regret3" -> binding.emotion.setImageResource(R.drawable.emotion_regret3)
			"sadness1" -> binding.emotion.setImageResource(R.drawable.emotion_sadness1)
			"sadness2" -> binding.emotion.setImageResource(R.drawable.emotion_sadness2)
			"sadness3" -> binding.emotion.setImageResource(R.drawable.emotion_sadness3)
			"tiredness1" -> binding.emotion.setImageResource(R.drawable.emotion_tiredness1)
			"tiredness2" -> binding.emotion.setImageResource(R.drawable.emotion_tiredness2)
			"tiredness3" -> binding.emotion.setImageResource(R.drawable.emotion_tiredness3)
			"neutrality" -> binding.emotion.setImageResource(R.drawable.emotion_neutrality)
		}
	}

	fun processEmotion(rank: Int, emotion: String, percent: Int?) {
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
			"anger" -> {
				txtEmotion.text = getString(R.string.anger)
				bar.progressDrawable = getDrawable(R.drawable.progress_anger)
			}
			"sadness" -> {
				txtEmotion.text = getString(R.string.sadness)
				bar.progressDrawable = getDrawable(R.drawable.progress_sadness)
			}
			"anxiety" -> {
				txtEmotion.text = getString(R.string.anxiety)
				bar.progressDrawable = getDrawable(R.drawable.progress_anxiety)
			}
			"tiredness" -> {
				txtEmotion.text = getString(R.string.tiredness)
				bar.progressDrawable = getDrawable(R.drawable.progress_tiredness)
			}
			"regret" -> {
				txtEmotion.text = getString(R.string.regret)
				bar.progressDrawable = getDrawable(R.drawable.progress_regret)
			}
			"joy" -> {
				txtEmotion.text = getString(R.string.happiness)
				bar.progressDrawable = getDrawable(R.drawable.progress_joy)
			}
			"hope" -> {
				txtEmotion.text = getString(R.string.hope)
				bar.progressDrawable = getDrawable(R.drawable.progress_hope)
			}

		}
		(percent.toString()+"%").also { txtPercent.text = it }
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
