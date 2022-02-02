package com.nicer.attiary.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.nicer.attiary.R
import com.nicer.attiary.data.app.AppLock
import com.nicer.attiary.view.SettingPasswordActivity
import com.nicer.attiary.data.app.AppLockConst
import com.nicer.attiary.data.app.lock
import com.nicer.attiary.data.user.User
import com.nicer.attiary.data.user.UserHelper
import com.nicer.attiary.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

	private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
	private var helper: UserHelper? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_sign_up)

		helper = Room.databaseBuilder(this, UserHelper::class.java, "user")
			.allowMainThreadQueries()
			.build()

		binding.buttonSubmit.setOnClickListener {
//			TODO, @jueun: parameter validation
			val user = User.Builder(binding.editEmail.text.toString(), binding.editName.text.toString())
				// TODO, @jueun: birthday binding
				.birthdayMonth(1)
				.birthdayDay(1)
				.build()
			helper?.userDao()?.insert(user)
		}

		val intent = Intent(this, SettingPasswordActivity::class.java)
		binding.buttonSettingPassword.setOnClickListener {
			startActivity(intent)
		}
	}

}