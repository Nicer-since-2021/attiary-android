package com.nicer.attiary.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.R
import com.nicer.attiary.view.auth.sign_up.SignUpActivity
import com.nicer.attiary.view.common.GlobalApplication
import com.nicer.attiary.view.signature.HomeActivity

class SplashActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)
		Handler(Looper.getMainLooper()).postDelayed({
			if (GlobalApplication.settingPrefs.isExist())
				startActivity(Intent(this, HomeActivity::class.java))
			else
				startActivity(Intent(this, SignUpActivity::class.java))
			finish()
		}, 2000)
	}
}