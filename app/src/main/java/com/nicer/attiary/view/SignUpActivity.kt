package com.nicer.attiary.view

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.nicer.attiary.R
import com.nicer.attiary.data.user.User
import com.nicer.attiary.data.user.UserHelper
import com.nicer.attiary.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

	private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
	private var helper: UserHelper? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		helper = Room.databaseBuilder(this, UserHelper::class.java, "user")
			.allowMainThreadQueries()
			.build()

		binding.buttonBack.setOnClickListener {
			finish()
		}

		val intent = Intent(this, SettingPasswordActivity::class.java)
		binding.buttonSettingPassword.setOnClickListener {
			startActivity(intent)
		}

		ArrayAdapter.createFromResource(
			this, R.array.months_array, android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			binding.spinnerMonth.adapter = adapter
		}

		ArrayAdapter.createFromResource(
			this, R.array.days_array, android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			binding.spinnerDay.adapter = adapter
		}

		binding.buttonSubmit.setOnClickListener {
			val email = binding.editEmail.text.toString()
			val name = binding.editName.text.toString()
			val user = saveUser(email, name)
			Toast.makeText(this, "$user 저장됨!", Toast.LENGTH_SHORT).show()
		}
	}

	private fun saveUser(email: String, name: String): User {
		val user =
			User.Builder(email, name)
				.birthdayMonth(binding.spinnerMonth.selectedItem.toString().toInt())
				.birthdayDay(binding.spinnerDay.selectedItem.toString().toInt())
				.build()
		helper?.userDao()?.insert(user)
		return user
	}

	private fun isValidEmail(email: String): Boolean {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
	}
}
