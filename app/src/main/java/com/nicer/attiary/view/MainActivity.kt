package com.nicer.attiary.view

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_USER_ACTION
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.R
import com.nicer.attiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

	private var signiture_bgm: MediaPlayer? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		signiture_bgm = MediaPlayer.create(this, R.raw.bluedream)
		signiture_bgm?.setLooping(true)
		signiture_bgm?.start()

		binding.btnSay.setOnClickListener {
			binding.textSay.text = "Hello, Kotlin!"
		}

		val intent = Intent(this, SignUpActivity::class.java)
		binding.btnStart.setOnClickListener {
			intent.addFlags(FLAG_ACTIVITY_NO_USER_ACTION)
			startActivity(intent)
		}

		val intent_signin = Intent(this, SignInActivity::class.java)
		binding.btnGotoSignin.setOnClickListener {
			intent_signin.addFlags(FLAG_ACTIVITY_NO_USER_ACTION)
			startActivity(intent_signin)
		}

		binding.btnStopBGM.setOnClickListener {
			signiture_bgm?.reset()
			signiture_bgm?.release()
			signiture_bgm = null
		}
	}

	override fun onUserLeaveHint() {
		super.onUserLeaveHint()
		signiture_bgm?.pause()
	}

	override fun onResume() {
		super.onResume()
		signiture_bgm?.start()
	}
}