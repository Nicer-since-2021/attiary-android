package com.nicer.attiary.view.ready

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.nicer.attiary.R
import com.nicer.attiary.view.MainActivity

class SplashActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)
		Handler().postDelayed({
//			startActivity(Intent(this, SettingPasswordActivity::class.java))
			startActivity(Intent(this, MainActivity::class.java))
			finish()
		}, 2000)
	}


}