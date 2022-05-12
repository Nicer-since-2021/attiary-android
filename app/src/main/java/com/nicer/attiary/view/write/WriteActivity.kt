package com.nicer.attiary.view.write

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.nicer.attiary.R
import com.nicer.attiary.data.api.nlp.RetrofitObject
import com.nicer.attiary.data.api.nlp.chat.Chat
import com.nicer.attiary.data.api.nlp.classification.Classification
import com.nicer.attiary.data.diary.DiaryList
import com.nicer.attiary.data.password.AppLock
import com.nicer.attiary.data.report.Report
import com.nicer.attiary.data.report.ReportDatabase
import com.nicer.attiary.databinding.ActivityWriteBinding
import com.nicer.attiary.view.common.AppPassWordActivity
import com.nicer.attiary.view.signature.DiaryActivity
import com.nicer.attiary.view.signature.MusicService
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteActivity : AppCompatActivity() {
    val binding by lazy { ActivityWriteBinding.inflate(layoutInflater) }

    private val viewModel: MusicViewModel by viewModels()
    lateinit var str: String
    private var database: ReportDatabase? = null
    lateinit var sigmu_intent: Intent
    var emoMP: MediaPlayer? = null
    private var cnt: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sigmu_intent = Intent(this, MusicService::class.java)
        stopService(sigmu_intent)

        val intent: Intent = getIntent()
        val year = intent.getIntExtra("year", 0)
        val month = intent.getIntExtra("month", 0)
        val dayOfMonth = intent.getIntExtra("dayOfMonth", 0)
        val rDate = (year.toString() + month.toString() + dayOfMonth.toString()).toLong()
        str = intent.getStringExtra("diary").toString()
        database = ReportDatabase.getInstance(this, Gson())

        binding.diaryTextView.text = String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth)
        binding.contextEditText.setText(str)

        shuffleTrack()

        binding.contextEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus)
                removeFragment()
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.saveBtn.setOnClickListener {
            startService(sigmu_intent)

            hideKeyboard()
            if (binding.contextEditText.text.isBlank()) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("내용을 입력하세요.")
                builder.setPositiveButton("확인", null)
                builder.show()
            } else {
                if (str != "null") {
                    DiaryList(this).removeDiary(CalendarDay(year, month, dayOfMonth))
                }

                //로딩화면

                val content = binding.contextEditText.text.toString()
                var emotions = hashMapOf<String, Int>()
                var dDepression = 0
                RetrofitObject.getApiService().getReport(content)
                    .enqueue(object : Callback<Classification> {
                        override fun onResponse(
                            call: Call<Classification>,
                            response: Response<Classification>
                        ) {
                            Log.d("YMC", "ㅎ")
                            if (response.isSuccessful) {
                                var result: Classification? = response.body()
                                Log.d("YMC", "onResponse 성공: ")
                                emotions.put("anger", ((result?.anger)?.times(100))?.toInt()!!)
                                emotions.put("anxiety", ((result.anxiety)?.times(100))?.toInt()!!)
                                emotions.put("hope", ((result.hope)?.times(100))?.toInt()!!)
                                emotions.put("joy", ((result.joy)?.times(100))?.toInt()!!)
                                emotions.put("regret", ((result.regret)?.times(100))?.toInt()!!)
                                emotions.put("sadness", ((result.sadness)?.times(100))?.toInt()!!)
                                emotions.put(
                                    "tiredness",
                                    ((result.tiredness)?.times(100))?.toInt()!!
                                )
                                dDepression = (result.depression)?.times(100)?.toInt()!!
                                Log.d("result", result.toString())
                            } else {
                                // 통신 실패
                                Log.d("YMC", "onResponse 실패")
                            }
                        }

                        override fun onFailure(call: Call<Classification>, t: Throwable) {
                            // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                            Log.d("YMC", "onFailure 에러: " + t.message.toString())
                        }
                    })

                Handler(Looper.getMainLooper()).postDelayed({
                    try {
                        Log.d("emotions", emotions.toString())
                        val emotions2 = emotions.toList().sortedByDescending { (_, value) -> value }
                            .toMap() as HashMap<String, Int>
                        val e1 = emotions2.keys.elementAt(0)
                        val p1 = emotions2.getValue(e1)
                        var happiness = emotions.get("joy")?.plus(emotions.get("hope")!!)
                        var depression = emotions.get("anger")?.plus(emotions.get("sadness")!!)
                            ?.plus(emotions.get("anxiety")!!)?.plus(emotions.get("tiredness")!!)
                            ?.plus(emotions.get("regret")!!)?.plus(dDepression)
                        var representative = setRepresentative(e1, p1)
                        CoroutineScope(Dispatchers.IO).launch {
                            database?.ReportDao()?.insert(
                                Report(
                                    rDate,
                                    content,
                                    representative,
                                    emotions,
                                    happiness!!,
                                    depression!!,
                                    ""
                                )
                            )
                        }
                        if (p1 == 0)
                            DiaryList(this).addDiary(
                                CalendarDay(year, month, dayOfMonth),
                                "neurality"
                            )
                        else
                            DiaryList(this).addDiary(CalendarDay(year, month, dayOfMonth), e1)
                    } catch (e: ClassCastException) {
                        Log.d("[error]", "ClassCastException")
                        //로딩화면 닫고
                        //서버가 불안정하니 다음에 다시 시도하라는 창: 일기내용만 저장해둘게요! 분석을 다시 시도하려면 수정 버튼을 누르고 다시 저장해보세요!
                        emotions.put("anger", 0)
                        emotions.put("anxiety", 0)
                        emotions.put("hope", 0)
                        emotions.put("joy", 0)
                        emotions.put("regret", 0)
                        emotions.put("sadness", 0)
                        emotions.put("tiredness", 0)

                        var happiness = emotions.get("joy")?.plus(emotions.get("hope")!!)
                        var depression = emotions.get("anger")?.plus(emotions.get("sadness")!!)
                            ?.plus(emotions.get("anxiety")!!)?.plus(emotions.get("tiredness")!!)
                            ?.plus(emotions.get("regret")!!)?.plus(dDepression)
                        var representative = "neutrality"
                        CoroutineScope(Dispatchers.IO).launch {
                            database?.ReportDao()?.insert(
                                Report(
                                    rDate,
                                    content,
                                    representative,
                                    emotions,
                                    happiness!!,
                                    depression!!,
                                    ""
                                )
                            )
                        }
                        DiaryList(this).addDiary(CalendarDay(year, month, dayOfMonth), "neutrality")
                    } finally {
                        //로딩화면 닫힘
                        val intent = Intent(this, DiaryActivity::class.java)
                        intent.putExtra("year", year)
                        intent.putExtra("month", month)
                        intent.putExtra("dayOfMonth", dayOfMonth)
                        startActivity(intent)
                        finish()
                    }
                }, 10000)


            }
        }
        
		binding.saveBtn.setOnClickListener {
			startService(intent_music)
			hideKeyboard()
			if (binding.contextEditText.text.isBlank()) {
				val builder = AlertDialog.Builder(this)
				builder.setMessage("내용을 입력하세요.")
				builder.setPositiveButton("확인", null)
				builder.show()
			} else {
				if (str != "null") {
					DiaryList(this).removeDiary(CalendarDay(year, month, dayOfMonth))
				}

				binding.wholeView.bringToFront()
				setLoadingFrag()

				val content = binding.contextEditText.text.toString()
				var emotions = hashMapOf<String, Int>()
				var dDepression = 0

				CoroutineScope(Dispatchers.IO).async {
					RetrofitObject.getApiService().getReport(content)?.enqueue(object : Callback<Classification> {
						override fun onResponse(call: Call<Classification>, response: Response<Classification>) {
							Log.d("YMC", "ㅎ")
							if (response.isSuccessful) {
								var result: Classification? = response.body()
								Log.d("YMC", "onResponse 성공: ")
								emotions.put("anger", ((result?.anger)?.times(100))?.toInt()!!)
								emotions.put("anxiety", ((result?.anxiety)?.times(100))?.toInt()!!)
								emotions.put("hope", ((result?.hope)?.times(100))?.toInt()!!)
								emotions.put("joy", ((result?.joy)?.times(100))?.toInt()!!)
								emotions.put("regret", ((result?.regret)?.times(100))?.toInt()!!)
								emotions.put("sadness", ((result?.sadness)?.times(100))?.toInt()!!)
								emotions.put("tiredness", ((result?.tiredness)?.times(100))?.toInt()!!)
								dDepression = (result?.depression)?.times(100)?.toInt()!!
								Log.d("result", result.toString())
							} else {
								// 통신 실패
								Log.d("YMC", "onResponse 실패")
							}
						}

						override fun onFailure(call: Call<Classification>, t: Throwable) {
							// 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
							Log.d("YMC", "onFailure 에러: " + t.message.toString());
						}
					})
				}
        

				Handler(Looper.getMainLooper()).postDelayed({
					try{
						Log.d("emotions", emotions.toString())
						val emotions2 = emotions.toList().sortedByDescending{ (_, value) -> value}.toMap() as HashMap<String, Int>
						val e1 = emotions2.keys.elementAt(0)
						val p1 = emotions2.getValue(e1)
						var happiness = emotions.get("joy")?.plus(emotions.get("hope")!!)
						var depression = emotions.get("anger")?.plus(emotions.get("sadness")!!)?.plus(emotions.get("anxiety")!!)?.plus(emotions.get("tiredness")!!)?.plus(emotions.get("regret")!!)?.plus(dDepression)
						var representative = setRepresentative(e1, p1)
						CoroutineScope(Dispatchers.IO).launch {
							database?.ReportDao()?.insert(
								Report(rDate, content, representative, emotions, happiness!!, depression!!, "")
							)
						}

						if (p1==0){
							DiaryList(this).addDiary(CalendarDay(year, month, dayOfMonth), "neutrality")
							Log.d("감정", "중립")
						}

						else
							DiaryList(this).addDiary(CalendarDay(year, month, dayOfMonth), e1)

						val intent = Intent(this, DiaryActivity::class.java)
						intent.putExtra("year", year)
						intent.putExtra("month", month)
						intent.putExtra("dayOfMonth", dayOfMonth)
						startActivity(intent)
						finish()
					}catch (e: ClassCastException){
						Log.d("[error]", "ClassCastException")

						emotions.put("anger", 0)
						emotions.put("anxiety", 0)
						emotions.put("hope", 0)
						emotions.put("joy", 0)
						emotions.put("regret", 0)
						emotions.put("sadness", 0)
						emotions.put("tiredness", 0)

						var happiness = emotions.get("joy")?.plus(emotions.get("hope")!!)
						var depression = emotions.get("anger")?.plus(emotions.get("sadness")!!)?.plus(emotions.get("anxiety")!!)?.plus(emotions.get("tiredness")!!)?.plus(emotions.get("regret")!!)?.plus(dDepression)
						var representative = "neutrality"
						CoroutineScope(Dispatchers.IO).launch {
							database?.ReportDao()?.insert(
								Report(rDate, content, representative, emotions, happiness!!, depression!!, "")
							)
						}
						DiaryList(this).addDiary(CalendarDay(year, month, dayOfMonth), "error")
						val intent = Intent(this, DiaryActivity::class.java)
						intent.putExtra("year", year)
						intent.putExtra("month", month)
						intent.putExtra("dayOfMonth", dayOfMonth)
						intent.putExtra("data", 0)
						startActivity(intent)
						finish()
					}
				}, 10000)
			}
		}

        binding.btnMusic.setOnLongClickListener {
            setFragment(MusicPopupFragment())
            true
        }

        binding.btnMusic.setOnClickListener {
            if (emoMP?.isPlaying == true) {
                emoMP?.pause()
                binding.btnMusic.setImageResource(R.drawable.music_mute_button)
            } else {
                emoMP?.start()
                binding.btnMusic.setImageResource(R.drawable.music_button)
            }
        }

        // 엔터키 누를 때마다 Chatbot 응답 전송
        binding.contextEditText.setOnKeyListener { _, keyCode, event ->

            if ((event.action == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                Log.d("YMC", "엔터키 입력")
                val str = binding.contextEditText.text.toString()
                val str_ = str.substring(cnt)
                RetrofitObject.getApiService().getChatRes(str_).enqueue(object : Callback<Chat> {
                    override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
                        if (response.isSuccessful) {
                            var result: Chat? = response.body()
                            Log.d("YMC", "onResponse 성공: " + result?.answer)
                            binding.attiMsgTxt.text = result?.answer
                        } else {
                            // 통신 실패
                            Log.d("YMC", "onResponse 실패")
                        }
                    }

                    override fun onFailure(call: Call<Chat>, t: Throwable) {
                        // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                        Log.d("YMC", "onFailure 에러: " + t.message.toString())
                    }
                })
                cnt = str.length
                true
            } else {
                false
            }


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

    fun hideKeyboard() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(binding.contextEditText.windowToken, 0)
    }

    private fun setRepresentative(e1: String, p1: Int): String {
        return when {
            p1 >= 66 -> (e1 + "3")
            p1 >= 33 -> (e1 + "2")
            p1 == 0 -> ("neutrality")
            else -> (e1 + "1")
        }
    }

    private fun setFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .setCustomAnimations(R.anim.musicpopup_open, R.anim.fade_out)
            .replace(R.id.frameLayout, MusicPopupFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun removeFragment() {
        val frameLayout = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (frameLayout != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction
                .setCustomAnimations(R.anim.musicpopup_close, R.anim.musicpopup_close)
                .remove(frameLayout)
                .commit()
        }
    }

	private fun setLoadingFrag() {
		val transaction = supportFragmentManager.beginTransaction()
		transaction
			.replace(R.id.wholeView, LoadingFragment())
			.addToBackStack(null)
			.commit()
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

        playTrack(MusicList.musicList.bgm_n_list)
        if (emoMP != null && emoMP?.isPlaying == false) {
            emoMP?.start()
        }

        cnt = 0
    }

    override fun onPause() {
        super.onPause()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        if (emoMP?.isPlaying == true) {
            emoMP?.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (emoMP != null) {
            emoMP?.stop()
            emoMP?.release()
        }
    }

    private fun shuffleTrack() {
        MusicList.musicList.bgm_a_list = MusicList.musicList.bgm_a_list.shuffled()
        MusicList.musicList.bgm_ax_list = MusicList.musicList.bgm_ax_list.shuffled()
        MusicList.musicList.bgm_ha_list = MusicList.musicList.bgm_ha_list.shuffled()
        MusicList.musicList.bgm_ho_list = MusicList.musicList.bgm_ho_list.shuffled()
        MusicList.musicList.bgm_n_list = MusicList.musicList.bgm_n_list.shuffled()
        MusicList.musicList.bgm_r_list = MusicList.musicList.bgm_r_list.shuffled()
        MusicList.musicList.bgm_s_list = MusicList.musicList.bgm_s_list.shuffled()
        MusicList.musicList.bgm_t_list = MusicList.musicList.bgm_t_list.shuffled()
    }

    private fun playTrack(list: List<Int>) {
        var tmpList = list
        if (tmpList.isEmpty()) {
            tmpList = list
        }

        val nextTrack = tmpList.first()
        tmpList = tmpList - nextTrack

        emoMP = MediaPlayer.create(this, nextTrack).apply {
            setOnCompletionListener {
                it.stop()
                it.release()
                playTrack(tmpList)
            }
            start()
        }
    }
}

