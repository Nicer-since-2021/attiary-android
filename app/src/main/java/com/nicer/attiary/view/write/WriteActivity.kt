package com.nicer.attiary.view.write

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.nicer.attiary.R
import com.nicer.attiary.data.diary.DiaryList
import com.nicer.attiary.data.password.AppLock
import com.nicer.attiary.data.report.Report
import com.nicer.attiary.data.report.ReportDatabase
import com.nicer.attiary.databinding.ActivityWriteBinding
import com.nicer.attiary.view.setting.lock.AppPassWordActivity
import com.nicer.attiary.view.signature.DiaryActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WriteActivity : AppCompatActivity() {
	val binding by lazy { ActivityWriteBinding.inflate(layoutInflater) }

	private val viewModel: MusicViewModel by viewModels()

	lateinit var fname: String
	lateinit var str: String
	private var database: ReportDatabase? = null
	var isExist : Boolean = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		database = ReportDatabase.getInstance(this)
		setContentView(binding.root)
		val intent: Intent = getIntent()
		var year = intent.getIntExtra("year", 0)
		var month = intent.getIntExtra("month", 0)
		var dayOfMonth = intent.getIntExtra("dayOfMonth", 0)
		str = intent.getStringExtra("diary").toString()
		var rDate = (year.toString() + month.toString() + dayOfMonth.toString()).toLong()

		binding.backBtn.setOnClickListener {
			finish()
		}

		binding.diaryTextView.text = String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth)
		binding.contextEditText.setText(str)


		binding.saveBtn.setOnClickListener {
			if (binding.contextEditText.text.isBlank()) {
				val builder = AlertDialog.Builder(this)
				builder.setMessage("내용을 입력하세요.")
				builder.setPositiveButton("확인", null)
				builder.show()
			} else {
				if (str!="null"){
					CoroutineScope(Dispatchers.IO).launch {
						database?.ReportDao()?.delete(rDate)
					}
					DiaryList(this).removeDiary(rDate)
				}
				CoroutineScope(Dispatchers.IO).launch {
					database?.ReportDao()?.insert(
						Report(rDate, binding.contextEditText.text.toString(), "a", 50, "s", 30, "ax", 20, "ha", 10, "")
					)
				}
				DiaryList(this).addDiary(rDate, "a") //대표감정 전달
				val intent: Intent = Intent(this, DiaryActivity::class.java)
				intent.putExtra("year", year)
				intent.putExtra("month", month)
				intent.putExtra("dayOfMonth", dayOfMonth)
				startActivity(intent)
				finish()
			}

		}

		binding.btnMusic.setOnLongClickListener {
			viewFragment(MusicPopupFragment())
			true
		}

		// Fragment 통신
		viewModel.getEmotion.observe(this, Observer { item ->
			Log.d("Selected Emotion", item.toString())
		})
	}

	fun viewFragment(fragment: Fragment?) {
		if (fragment != null) {
			supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
		}
	}


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