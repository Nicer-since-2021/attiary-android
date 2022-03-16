package com.nicer.attiary.view.setting.lock


import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.databinding.ActivitySettingPwBinding
import com.nicer.attiary.data.password.AppLock
import com.nicer.attiary.data.password.AppLock.AppLockStatus.lock


class SettingPasswordActivity : AppCompatActivity() {

	val binding by lazy { ActivitySettingPwBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		init()

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

		binding.btnSetLock.setOnClickListener {
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra("type", AppLock.AppLockStatus.ENABLE_PASSLOCK)
			}
			activityResult.launch(intent)
		}

		binding.btnSetDelLock.setOnClickListener{
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra("type", AppLock.AppLockStatus.DISABLE_PASSLOCK)
			}
			activityResult.launch(intent)
		}

		binding.btnChangePwd.setOnClickListener {
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra("type", AppLock.AppLockStatus.CHANGE_PASSWORD)
			}
			activityResult.launch(intent)
		}

	}

	private fun init(){
		if (AppLock(this).isPassLockSet()){
			binding.btnSetLock.isEnabled = false
			binding.btnSetDelLock.isEnabled = true
			binding.btnChangePwd.isEnabled = true
		}
		else{
			binding.btnSetLock.isEnabled = true
			binding.btnSetDelLock.isEnabled = false
			binding.btnChangePwd.isEnabled = false
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
