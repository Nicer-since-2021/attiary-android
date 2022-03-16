package com.nicer.attiary.view.auth.sign_up

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.nicer.attiary.R
import com.nicer.attiary.data.password.AppLock
import com.nicer.attiary.data.user.User
import com.nicer.attiary.data.user.UserHelper
import com.nicer.attiary.databinding.ActivitySignUpBinding
import com.nicer.attiary.view.setting.lock.AppPassWordActivity
import com.nicer.attiary.view.setting.lock.SettingPasswordActivity
import com.nicer.attiary.view.signature.HomeActivity

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

		val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
			val returnCode = it.data?.getIntExtra("returnCode", 0)
			if (it.resultCode == RESULT_OK) {
				when (returnCode) {
					AppLock.AppLockStatus.ENABLE_PASSLOCK -> {
						Toast.makeText(this, "암호 설정 완료", Toast.LENGTH_SHORT).show()
						AppLock.AppLockStatus.lock = false
					}
				}
			}
		}
		binding.buttonSettingPassword.setOnClickListener {
			if(AppLock(this).isPassLockSet()){
				val builder = AlertDialog.Builder(this)
				builder.setMessage("이미 비밀번호를 설정하였습니다.")
				builder.setPositiveButton("확인", null)
				builder.show()
			}
			else{
				val intent = Intent(this, AppPassWordActivity::class.java).apply {
					putExtra("type", AppLock.AppLockStatus.ENABLE_PASSLOCK)
				}
				activityResult.launch(intent)
			}
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
			startActivity(Intent(this, HomeActivity::class.java))
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