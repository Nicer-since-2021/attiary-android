package com.nicer.attiary.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nicer.attiary.R
import com.nicer.attiary.data.app.AppLock
import com.nicer.attiary.data.app.AppLockConst
import com.nicer.attiary.data.app.lock
import kotlinx.android.synthetic.main.activity_app_password.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting_pw.*

class SettingPasswordActivity : AppCompatActivity() {
	// 잠금 상태 여부 확인

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_setting_pw)

		init()

		// 잠금 설정 버튼을 눌렀을때
		btnSetLock.setOnClickListener {
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra(AppLockConst.AppLockCosnt.type, AppLockConst.AppLockCosnt.ENABLE_PASSLOCK)
			}
			startActivityForResult(intent, AppLockConst.AppLockCosnt.ENABLE_PASSLOCK)

		}

		// 잠금 비활성화 버튼을 눌렀을때
		btnSetDelLock.setOnClickListener{
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra(AppLockConst.AppLockCosnt.type, AppLockConst.AppLockCosnt.DISABLE_PASSLOCK)
			}
			startActivityForResult(intent, AppLockConst.AppLockCosnt.DISABLE_PASSLOCK)
		}

		// 암호 변경버튼을 눌렀을때
		btnChangePwd.setOnClickListener {
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
			btnSetLock.isEnabled = false
			btnSetDelLock.isEnabled = true
			btnChangePwd.isEnabled = true
			lock = true
		}
		else{
			btnSetLock.isEnabled = true
			btnSetDelLock.isEnabled = false
			btnChangePwd.isEnabled = false
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
