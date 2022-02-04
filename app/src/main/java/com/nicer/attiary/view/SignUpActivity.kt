package com.nicer.attiary.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
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
		setContentView(R.layout.activity_sign_up)

		helper = Room.databaseBuilder(this, UserHelper::class.java, "user")
			.allowMainThreadQueries()
			.build()

		/**
		 * convert findViewById to viewbinding
		 */
		val buttonBack = findViewById<ImageButton>(R.id.buttonBack)
		buttonBack.setOnClickListener {
			finish()
		}
/*
		binding.backButton.setOnClickListener {
			finish()
		}
*/


		/**
		 * convert findViewById to viewbinding
		 */
		val intent = Intent(this, SettingPasswordActivity::class.java)
		val buttonSettingPassword = findViewById<Button>(R.id.buttonSettingPassword)
		buttonSettingPassword.setOnClickListener {
			startActivity(intent)
		}
/*
		binding.buttonSettingPassword.setOnClickListener {
			startActivity(intent)
		}
*/


		/**
		 * convert findViewById to viewbinding
		 */
		val spinnerMonth = findViewById<Spinner>(R.id.spinnerMonth)
		ArrayAdapter.createFromResource(
			this, R.array.months_array, android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			spinnerMonth.adapter = adapter
		}

		val spinnerDay = findViewById<Spinner>(R.id.spinnerDay)
		ArrayAdapter.createFromResource(
			this, R.array.days_array, android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			spinnerDay.adapter = adapter
		}
/*
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
*/

		/**
		 * convert findViewById to viewbinding
		 */
		val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
		val e = findViewById<EditText>(R.id.editEmail)
		val n = findViewById<EditText>(R.id.editName)
		buttonSubmit.setOnClickListener {
			val user =
				User.Builder(e.text.toString(), n.text.toString())
					.birthdayMonth(spinnerMonth.selectedItem.toString().toInt())
					.birthdayDay(spinnerDay.selectedItem.toString().toInt())
					.build()
			Log.d("user info", user.toString())
			helper?.userDao()?.insert(user)
		}
/*
		binding.buttonSubmit.setOnClickListener {
//			TODO, @jueun: parameter validation
			val user =
				User.Builder(binding.editEmail.text.toString(), binding.editName.text.toString())
					.birthdayMonth(binding.spinnerMonth.selectedItem.toString().toInt())
					.birthdayDay(binding.spinnerDay.selectedItem.toString().toInt())
					.build()
			helper?.userDao()?.insert(user)
		}
*/
	}
}
