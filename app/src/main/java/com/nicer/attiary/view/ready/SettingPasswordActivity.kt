package com.nicer.attiary.view.ready


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nicer.attiary.data.app.AppLock
import com.nicer.attiary.data.app.AppLockConst
import com.nicer.attiary.data.app.lock
import com.nicer.attiary.databinding.ActivitySettingPwBinding


class SettingPasswordActivity : AppCompatActivity() {

	val binding by lazy { ActivitySettingPwBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)


		init()

		// 잠금 설정 버튼을 눌렀을때
		binding.btnSetLock.setOnClickListener {
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra(AppLockConst.AppLockCosnt.type, AppLockConst.AppLockCosnt.ENABLE_PASSLOCK)
			}
			startActivityForResult(intent, AppLockConst.AppLockCosnt.ENABLE_PASSLOCK)

		}

		// 잠금 비활성화 버튼을 눌렀을때
		binding.btnSetDelLock.setOnClickListener{
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra(AppLockConst.AppLockCosnt.type, AppLockConst.AppLockCosnt.DISABLE_PASSLOCK)
			}
			startActivityForResult(intent, AppLockConst.AppLockCosnt.DISABLE_PASSLOCK)
		}

		// 암호 변경버튼을 눌렀을때
		binding.btnChangePwd.setOnClickListener {
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra(AppLockConst.AppLockCosnt.type, AppLockConst.AppLockCosnt.CHANGE_PASSWORD)
			}
			startActivityForResult(intent, AppLockConst.AppLockCosnt.CHANGE_PASSWORD)

		}
	}

	// startActivityForResult 결과값을 받는다.
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		when(requestCode){
			AppLockConst.AppLockCosnt.ENABLE_PASSLOCK ->
				if(resultCode == RESULT_OK){
					Toast.makeText(this, "암호 설정 완료", Toast.LENGTH_SHORT).show()
					init()
					lock  = false
				}

			AppLockConst.AppLockCosnt.DISABLE_PASSLOCK ->
				if(resultCode == RESULT_OK){
					Toast.makeText(this, "암호 삭제 완료", Toast.LENGTH_SHORT).show()
					init()
				}

			AppLockConst.AppLockCosnt.CHANGE_PASSWORD ->
				if(resultCode == RESULT_OK){
					Toast.makeText(this, "암호 변경 완료", Toast.LENGTH_SHORT).show()
					lock = false
				}

			AppLockConst.AppLockCosnt.UNLOCK_PASSWORD ->
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
				putExtra(AppLockConst.AppLockCosnt.type, AppLockConst.AppLockCosnt.UNLOCK_PASSWORD)
			}
			startActivityForResult(intent, AppLockConst.AppLockCosnt.UNLOCK_PASSWORD)
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
