package com.nicer.attiary.view.setting


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nicer.attiary.R
import com.nicer.attiary.data.diary.DiaryList
import com.nicer.attiary.data.password.AppLock
import com.nicer.attiary.data.password.AppLock.AppLockStatus.lock
import com.nicer.attiary.data.user.UserHelper
import com.nicer.attiary.databinding.ActivitySettingBinding
import com.nicer.attiary.view.common.AppPassWordActivity
import com.nicer.attiary.view.common.GlobalApplication
import com.nicer.attiary.view.signature.MusicService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingActivity : AppCompatActivity() {

    val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }
    var helper: UserHelper? = null
    lateinit var sigmu_intent: Intent

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

        init()
		var isNotCanceled = true
        helper = UserHelper.getInstance(this)
        sigmu_intent = Intent(this, MusicService::class.java)
		binding.wholeView.bringToFront()

		val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
			val returnCode = it.data?.getIntExtra("returnCode", 0)
			if (it.resultCode == RESULT_OK){
				when (returnCode){
					AppLock.AppLockStatus.ENABLE_PASSLOCK ->{
						Toast.makeText(this, "암호 설정 완료", Toast.LENGTH_SHORT).show()
						lock  = false
					}

					AppLock.AppLockStatus.DISABLE_PASSLOCK ->{
						Toast.makeText(this, "암호 삭제 완료", Toast.LENGTH_SHORT).show()
					}

					AppLock.AppLockStatus.UNLOCK_PASSWORD ->
					{
						lock = false
					}

					AppLock.AppLockStatus.CHANGE_PASSWORD ->
					{
						Toast.makeText(this, "암호 변경 완료", Toast.LENGTH_SHORT).show()
						lock = false
					}
					AppLock.AppLockStatus.CANCEL ->
					{
						isNotCanceled = false
					}
				}
			}
		}

		binding.nicknameChangeBtn.setOnClickListener {
			binding.nicknameEdit.isVisible = true
			binding.nicknameSaveBtn.isVisible=true
			binding.nicknameChangeBtn.isVisible = false
			binding.nicknameText.isVisible=false
		}

		binding.nicknameSaveBtn.setOnClickListener {
			CoroutineScope(Dispatchers.IO).launch {
				helper?.userDao()?.updateName(binding.nicknameEdit.text.toString())
			}
			GlobalApplication.settingPrefs.setString("nickname", binding.nicknameEdit.text.toString())
			init()
			binding.nicknameEdit.isVisible = false
			binding.nicknameSaveBtn.isVisible=false
			binding.nicknameChangeBtn.isVisible = true
			binding.nicknameText.isVisible=true
		}

		binding.changeBdayBtn.setOnClickListener {
			binding.spinnerDay.isVisible=true
			binding.spinnerMonth.isVisible=true
			binding.textDay.isVisible=true
			binding.textMonth.isVisible=true
			binding.bdayText.isVisible=false
			binding.saveBdayBtn.isVisible=true
			binding.changeBdayBtn.isVisible=false
		}

		ArrayAdapter.createFromResource(
			this, R.array.months_array, android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			binding.spinnerMonth.adapter = adapter
		}

		ArrayAdapter.createFromResource(
			this, R.array.days_array, android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			binding.spinnerDay.adapter = adapter
		}

		binding.saveBdayBtn.setOnClickListener {
			CoroutineScope(Dispatchers.IO).launch {
				helper?.userDao()?.updateBdayDay(binding.spinnerDay.selectedItem.toString().toInt())
				helper?.userDao()?.updateBdayMonth(binding.spinnerMonth.selectedItem.toString().toInt())
			}
			init()
			binding.spinnerDay.isVisible=false
			binding.spinnerMonth.isVisible=false
			binding.textDay.isVisible=false
			binding.textMonth.isVisible=false
			binding.bdayText.isVisible=true
			binding.saveBdayBtn.isVisible=false
			binding.changeBdayBtn.isVisible=true
		}

        binding.sigMusicSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                GlobalApplication.settingPrefs.setString("sigMusic", "sON")
                startService(sigmu_intent)
            } else {
                GlobalApplication.settingPrefs.setString("sigMusic", "sOF")
                stopService(sigmu_intent)
            }
        }

		binding.chatBotSelectBtn.setOnClickListener{
			setChatbotFragment()
		}

		binding.pwSwitch.setOnCheckedChangeListener{CompoundButton, onSwitch ->
			if (isNotCanceled){
				if (onSwitch){
					val intent = Intent(this, AppPassWordActivity::class.java).apply {
						putExtra("type", AppLock.AppLockStatus.ENABLE_PASSLOCK)
					}
					activityResult.launch(intent)
				}
				else{
					val intent = Intent(this, AppPassWordActivity::class.java).apply {
						putExtra("type", AppLock.AppLockStatus.DISABLE_PASSLOCK)
					}
					activityResult.launch(intent)
				}
			}
			isNotCanceled = true
		}

		binding.changePWBtn.setOnClickListener {
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra("type", AppLock.AppLockStatus.CHANGE_PASSWORD)
			}
			activityResult.launch(intent)
		}

		ArrayAdapter.createFromResource(
			this, R.array.months_array, android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			binding.spinnerMonth.adapter = adapter
		}

		ArrayAdapter.createFromResource(
			this, R.array.days_array, android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			binding.spinnerDay.adapter = adapter
		}

		binding.closeBtn.setOnClickListener{
			finish()
		}

	}

	private fun init(){
		binding.myAccountText.isVisible = false
		binding.myAccountTitleText.isVisible = false
		binding.logoutBtn.isVisible = false
		binding.line3.isVisible = false
		val sigCheck = GlobalApplication.settingPrefs.getString("sigMusic","")
		binding.sigMusicSwitch.isChecked = sigCheck == "sON"
		binding.dDayText.text = "♥${DiaryList(this).getNumDiary().toString()}"


		if (AppLock(this).isPassLockSet()){
			binding.changePWBtn.isVisible = true
			binding.changePWText.isVisible = true
			binding.pwSwitch.isChecked = true
		}
		else{
			binding.changePWBtn.isVisible = false
			binding.changePWText.isVisible = false
			binding.pwSwitch.isChecked = false
		}
		var name = GlobalApplication.settingPrefs.getString("nickname", "")
		binding.nicknameText.text = name
		binding.nicknameEdit.setText(name)
		CoroutineScope(Dispatchers.IO).launch {
			var bDayDay = helper?.userDao()?.findByUserId(1)?.birthdayDay
			var bDayMonth = helper?.userDao()?.findByUserId(1)?.birthdayMonth
			CoroutineScope(Dispatchers.Main).launch {
				binding.bdayText.text = "$bDayMonth 월 $bDayDay 일"
			}
		}
	}

	private fun setChatbotFragment() {
		val transaction = supportFragmentManager.beginTransaction()
		transaction
			.replace(R.id.wholeView, SetChatbotFragment())
			.addToBackStack(null)
			.commit()
	}

	override fun onTouchEvent(event: MotionEvent): Boolean {
		val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
		return true
	}


	override fun onResume() {
		super.onResume()
		init()
		if (lock && AppLock(this).isPassLockSet()) {
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
