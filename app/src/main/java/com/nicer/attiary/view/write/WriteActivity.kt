package com.nicer.attiary.view.write

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
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
	lateinit var str: String
	private var database: ReportDatabase? = null
	var mp: MediaPlayer? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		val intent: Intent = getIntent()
		val year = intent.getIntExtra("year", 0)
		val month = intent.getIntExtra("month", 0)
		val dayOfMonth = intent.getIntExtra("dayOfMonth", 0)
		val rDate = (year.toString() + month.toString() + dayOfMonth.toString()).toLong()
		str = intent.getStringExtra("diary").toString()
		database = ReportDatabase.getInstance(this)


		binding.diaryTextView.text = String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth)
		binding.contextEditText.setText(str)

		shuffleTrack()

		binding.contextEditText.setOnFocusChangeListener { view, hasFocus ->
			if (hasFocus)
				RemoveFragment()
		}

		binding.backBtn.setOnClickListener {
			finish()
		}

		binding.saveBtn.setOnClickListener {
			if (binding.contextEditText.text.isBlank()) {
				val builder = AlertDialog.Builder(this)
				builder.setMessage("내용을 입력하세요.")
				builder.setPositiveButton("확인", null)
				builder.show()
			}
			else {
				if (str!="null"){
					DiaryList(this).removeDiary(rDate)
				}
				CoroutineScope(Dispatchers.IO).launch {
					database?.ReportDao()?.insert(
						Report(rDate, binding.contextEditText.text.toString(), "a", 50, "s", 30, "ax", 20, "ha", 10, "아띠의 한 마디~")
					)
				}
				DiaryList(this).addDiary(rDate, "a") //대표감정 전달
				val intent = Intent(this, DiaryActivity::class.java)
				intent.putExtra("year", year)
				intent.putExtra("month", month)
				intent.putExtra("dayOfMonth", dayOfMonth)
				startActivity(intent)
				finish()
			}
		}

		binding.btnMusic.setOnLongClickListener {
			SetFragment(MusicPopupFragment())
			true
		}

		// Fragment 통신
		viewModel.getEmotion.observe(this, Observer { item ->
			when (item) {
				0 -> playTrack(MusicList.musicList.bgm_ha_list)
				1 -> playTrack(MusicList.musicList.bgm_ho_list)
				2 -> playTrack(MusicList.musicList.bgm_s_list)
				3 -> playTrack(MusicList.musicList.bgm_a_list)
				4 -> playTrack(MusicList.musicList.bgm_ax_list)
				5 -> playTrack(MusicList.musicList.bgm_t_list)
				6 -> playTrack(MusicList.musicList.bgm_r_list)
			}
		})
	}


	fun SetFragment(fragment: Fragment?) {
		val transaction = supportFragmentManager.beginTransaction()
		transaction
			.setCustomAnimations(R.anim.musicpopup_open, R.anim.fade_out)
			.replace(R.id.frameLayout, MusicPopupFragment())
			.addToBackStack(null)
			.commit()
	}

	fun RemoveFragment() {
		val frameLayout = supportFragmentManager.findFragmentById(R.id.frameLayout)
		if (frameLayout != null) {
			val transaction = supportFragmentManager.beginTransaction()
			transaction
				.setCustomAnimations(R.anim.musicpopup_close, R.anim.musicpopup_close)
				.remove(frameLayout)
				.commit()
		}
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

	fun shuffleTrack() {
		MusicList.musicList.bgm_a_list = MusicList.musicList.bgm_a_list.shuffled()
		MusicList.musicList.bgm_ax_list = MusicList.musicList.bgm_ax_list.shuffled()
		MusicList.musicList.bgm_ha_list = MusicList.musicList.bgm_ha_list.shuffled()
		MusicList.musicList.bgm_ho_list = MusicList.musicList.bgm_ho_list.shuffled()
		MusicList.musicList.bgm_n_list = MusicList.musicList.bgm_n_list.shuffled()
		MusicList.musicList.bgm_r_list = MusicList.musicList.bgm_r_list.shuffled()
		MusicList.musicList.bgm_s_list = MusicList.musicList.bgm_s_list.shuffled()
		MusicList.musicList.bgm_t_list = MusicList.musicList.bgm_t_list.shuffled()
	}

	fun playTrack(list: List<Int>) {
		var tmpList = list
		if (tmpList.isEmpty()) {
			tmpList = list.shuffled()
		}

		val nextTrack = tmpList.first()
		tmpList = tmpList - nextTrack

		if (mp != null) {
			mp?.stop()
			mp?.release()
		}

		mp = MediaPlayer.create(this, nextTrack).apply {
			setOnCompletionListener {
				it.stop()
				it.release()
				playTrack(tmpList)
			}
			start()
		}
	}
}