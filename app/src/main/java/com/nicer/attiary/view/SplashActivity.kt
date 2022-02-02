package com.nicer.attiary.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.nicer.attiary.R
import com.nicer.attiary.data.app.AppLock
import com.nicer.attiary.data.app.AppLockConst
import com.nicer.attiary.data.app.lock

class SplashActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)
		Handler().postDelayed({
			startActivity(Intent(this, SettingPasswordActivity::class.java))
			finish()
		}, 2000)
	}


}