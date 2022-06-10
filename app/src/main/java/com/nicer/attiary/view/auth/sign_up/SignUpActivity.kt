package com.nicer.attiary.view.auth.sign_up

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.R
import com.nicer.attiary.data.password.AppLock
import com.nicer.attiary.data.user.User
import com.nicer.attiary.data.user.UserHelper
import com.nicer.attiary.databinding.ActivitySignUpBinding
import com.nicer.attiary.view.common.AppPassWordActivity
import com.nicer.attiary.view.common.GlobalApplication
import com.nicer.attiary.view.signature.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

	private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
	private var helper: UserHelper? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

//		helper = Room.databaseBuilder(this, UserHelper::class.java, "user")
//			.allowMainThreadQueries()
//			.build()
		helper = UserHelper.getInstance(this)

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
			if(name == "" || email == ""){
				val builder = AlertDialog.Builder(this)
				builder.setMessage("내용을 입력하세요.")
				builder.setPositiveButton("확인", null)
				builder.show()
			}else{
				saveUser(email, name)
				GlobalApplication.settingPrefs.setString("nickname", name)
				startActivity(Intent(this, HomeActivity::class.java))
				finish()
			}
		}
	}

	private fun saveUser(email: String, name: String): User {
		val user =
			User.Builder(email, name)
				.birthdayMonth(binding.spinnerMonth.selectedItem.toString().toInt())
				.birthdayDay(binding.spinnerDay.selectedItem.toString().toInt())
				.build()
		CoroutineScope(Dispatchers.IO).launch {
			helper?.userDao()?.insert(user)
			user.userId
		}
		return user
	}

	private fun isValidEmail(email: String): Boolean {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
	}
}