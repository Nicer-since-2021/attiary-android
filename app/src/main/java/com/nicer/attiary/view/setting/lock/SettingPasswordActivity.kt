package com.nicer.attiary.view.setting.lock


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.databinding.ActivitySettingPwBinding
import com.nicer.attiary.util.lock.AppLock
import com.nicer.attiary.util.lock.AppLockStatus
import com.nicer.attiary.util.lock.lock


class SettingPasswordActivity : AppCompatActivity() {

	val binding by lazy { ActivitySettingPwBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)


		init()

		// 잠금 설정 버튼을 눌렀을때
		binding.btnSetLock.setOnClickListener {
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra(AppLockStatus.AppLockStatus.TYPE, AppLockStatus.AppLockStatus.ENABLE_PASSLOCK)
			}
			startActivityForResult(intent, AppLockStatus.AppLockStatus.ENABLE_PASSLOCK)
		}

		// 잠금 비활성화 버튼을 눌렀을때
		binding.btnSetDelLock.setOnClickListener{
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra(AppLockStatus.AppLockStatus.TYPE, AppLockStatus.AppLockStatus.DISABLE_PASSLOCK)
			}
			startActivityForResult(intent, AppLockStatus.AppLockStatus.DISABLE_PASSLOCK)
		}

		// 암호 변경버튼을 눌렀을때
		binding.btnChangePwd.setOnClickListener {
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra(AppLockStatus.AppLockStatus.TYPE, AppLockStatus.AppLockStatus.CHANGE_PASSWORD)
			}
			startActivityForResult(intent, AppLockStatus.AppLockStatus.CHANGE_PASSWORD)
		}
	}

	// startActivityForResult 결과값을 받는다.
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		when(requestCode){
			AppLockStatus.AppLockStatus.ENABLE_PASSLOCK ->
				if(resultCode == RESULT_OK){
					Toast.makeText(this, "암호 설정 완료", Toast.LENGTH_SHORT).show()
					init()
					lock  = false
				}

			AppLockStatus.AppLockStatus.DISABLE_PASSLOCK ->
				if(resultCode == RESULT_OK){
					Toast.makeText(this, "암호 삭제 완료", Toast.LENGTH_SHORT).show()
					init()
				}

			AppLockStatus.AppLockStatus.CHANGE_PASSWORD ->
				if(resultCode == RESULT_OK){
					Toast.makeText(this, "암호 변경 완료", Toast.LENGTH_SHORT).show()
					lock = false
				}

			AppLockStatus.AppLockStatus.UNLOCK_PASSWORD ->
				if(resultCode == RESULT_OK){
					lock = false
				}
		}
	}

	// 버튼 비활성화
	private fun init(){
		if (AppLock(this).isPassLockSet()){
			binding.btnSetLock.isEnabled = false
			binding.btnSetDelLock.isEnabled = true
			binding.btnChangePwd.isEnabled = true
			lock = true
		}
		else{
			binding.btnSetLock.isEnabled = true
			binding.btnSetDelLock.isEnabled = false
			binding.btnChangePwd.isEnabled = false
			lock = false
		}
	}

	// 액티비티가 onStart인 경우
	override fun onResume() {
		super.onResume()
		if(lock && AppLock(this).isPassLockSet()){
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra(AppLockStatus.AppLockStatus.TYPE, AppLockStatus.AppLockStatus.UNLOCK_PASSWORD)
			}
			startActivityForResult(intent, AppLockStatus.AppLockStatus.UNLOCK_PASSWORD)
		}

	}

	// 액티비티가 onPause인경우
	override fun onPause() {
		super.onPause()
		if (AppLock(this).isPassLockSet()) {
			lock = true // 잠금으로 변경
		}
	}
}
