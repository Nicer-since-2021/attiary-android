package com.nicer.attiary.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.R
import com.nicer.attiary.view.auth.sign_up.SignUpActivity

class SplashActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)
		Handler().postDelayed({
//			startActivity(Intent(this, SettingPasswordActivity::class.java))
			startActivity(Intent(this, SignUpActivity::class.java))
			finish()
		}, 2000)
	}
}