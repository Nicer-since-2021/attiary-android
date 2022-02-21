package com.nicer.attiary.view.ready

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.R

class SplashActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)
		Handler().postDelayed({
//			startActivity(Intent(this, SettingPasswordActivity::class.java))
			startActivity(Intent(this, SignInActivity::class.java))
			finish()
		}, 2000)
	}


}