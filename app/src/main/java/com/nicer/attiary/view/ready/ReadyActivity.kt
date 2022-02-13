package com.nicer.attiary.view.ready

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.R
import com.nicer.attiary.databinding.ActivityReadyBinding

class ReadyActivity : AppCompatActivity() {

	val binding by lazy { ActivityReadyBinding.inflate(layoutInflater) }

	val splashFragment by lazy { SplashFragment() }
	val signInFragment by lazy { SignInFragment() }
	val signUpFragment by lazy { SignUpFragment() }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		setFragment()
	}

	fun setFragment(){
		// 트랜젝션 생성
		val transaction = supportFragmentManager.beginTransaction()

		// 트랜젝션을 통해 화면에 fragment 삽입
		transaction.add(R.id.frameLayout, splashFragment)
		transaction.commit()
	}
}