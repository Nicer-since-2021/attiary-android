package com.nicer.attiary.view.setting.lock


import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Switch
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.nicer.attiary.R
import com.nicer.attiary.databinding.ActivitySettingBinding
import com.nicer.attiary.data.password.AppLock
import com.nicer.attiary.data.password.AppLock.AppLockStatus.lock
import com.nicer.attiary.data.user.UserHelper
import com.nicer.attiary.view.common.AppPassWordActivity
import com.nicer.attiary.view.common.GlobalApplication
import com.nicer.attiary.view.signature.MusicService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingActivity : AppCompatActivity() {

	val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }
	var helper: UserHelper? = null
	lateinit var intent_music: Intent

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		init()
		helper = UserHelper.getInstance(this)
		intent_music = Intent(this, MusicService::class.java)

		val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
			val returnCode = it.data?.getIntExtra("returnCode", 0)
			if (it.resultCode == RESULT_OK){
				when (returnCode){
					AppLock.AppLockStatus.ENABLE_PASSLOCK ->{
						Toast.makeText(this, "암호 설정 완료", Toast.LENGTH_SHORT).show()
						init()
						lock  = false
					}

					AppLock.AppLockStatus.DISABLE_PASSLOCK ->{
						Toast.makeText(this, "암호 삭제 완료", Toast.LENGTH_SHORT).show()
						init()
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

		binding.saveBdayBtn.setOnClickListener {
			binding.spinnerDay.isVisible=false
			binding.spinnerMonth.isVisible=false
			binding.textDay.isVisible=false
			binding.textMonth.isVisible=false
			binding.bdayText.isVisible=true
			binding.saveBdayBtn.isVisible=false
			binding.changeBdayBtn.isVisible=true
		}

		binding.sigMusicSwitch.setOnCheckedChangeListener { _, isChecked ->
			if (isChecked){
				GlobalApplication.musicPrefs.setString("sigMusic", "sON")
				startService(intent_music)
			}
			else{
				GlobalApplication.musicPrefs.setString("sigMusic", "sOF")
				stopService(intent_music)
			}
		}

		val sigCheck = GlobalApplication.musicPrefs.getString("sigMusic","")
		binding.sigMusicSwitch.isChecked = sigCheck == "sON"

		binding.pwSwitch.setOnCheckedChangeListener{CompoundButton, onSwitch ->
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

		if (AppLock(this).isPassLockSet()){
			binding.changePWBtn.isVisible = true
			binding.changePWText.isVisible = true
		}
		else{
			binding.changePWBtn.isVisible = false
			binding.changePWText.isVisible = false
		}
		CoroutineScope(Dispatchers.IO).launch {
			var name = 	helper?.userDao()?.getName()?.toList()?.get(0)
			CoroutineScope(Dispatchers.Main).launch {
				binding.nicknameText.text = name
				binding.nicknameEdit.setText(name)
			}
		}
	}

	override fun onResume() {
		super.onResume()
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
