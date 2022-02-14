package com.nicer.attiary.view.ready


import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.R
import com.nicer.attiary.data.app.AppLock
import com.nicer.attiary.data.app.AppLockConst
import com.nicer.attiary.databinding.ActivityAppPasswordBinding


class AppPassWordActivity : AppCompatActivity(){

	val binding by lazy { ActivityAppPasswordBinding.inflate(layoutInflater) }
	private var oldPwd =""
	private var changePwdUnlock = false


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)


		binding.editPW1.showSoftInputOnFocus = false
		binding.editPW2.showSoftInputOnFocus = false
		binding.editPW3.showSoftInputOnFocus = false
		binding.editPW4.showSoftInputOnFocus = false
		binding.imgLocked1.bringToFront()
		binding.imgLocked1.isClickable = true
		binding.imgLocked2.bringToFront()
		binding.imgLocked2.isClickable = true
		binding.imgLocked3.bringToFront()
		binding.imgLocked3.isClickable = true
		binding.imgLocked4.bringToFront()
		binding.imgLocked4.isClickable = true

		val buttonArray = arrayListOf<Button>(binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4, binding.btn5, binding.btn6, binding.btn7 ,binding.btn8, binding.btn9, binding.btnDel)
		for (button in buttonArray){
			button.setOnClickListener(btnListener)
		}
		binding.btnPwCancel.setOnClickListener {
			finish()
		}

	}

	// 버튼 클릭 했을때
	private val btnListener = View.OnClickListener { view ->
		var currentValue = -1
		when(view.id){
			R.id.btn0 -> currentValue = 0
			R.id.btn1 -> currentValue = 1
			R.id.btn2 -> currentValue = 2
			R.id.btn3 -> currentValue = 3
			R.id.btn4 -> currentValue = 4
			R.id.btn5 -> currentValue = 5
			R.id.btn6 -> currentValue = 6
			R.id.btn7 -> currentValue = 7
			R.id.btn8 -> currentValue = 8
			R.id.btn9 -> currentValue = 9
			R.id.btnDel -> onDeleteKey()
		}

		val strCurrentValue = currentValue.toString() // 현재 입력된 번호 String으로 변경
		if (currentValue != -1){
			when {
				binding.editPW1.isFocused -> {
					binding.imgLocked1.setImageResource(R.drawable.unlocked_atti)
					setEditText(binding.editPW1, binding.editPW2, strCurrentValue)
				}
				binding.editPW2.isFocused -> {
					binding.imgLocked2.setImageResource(R.drawable.unlocked_atti)
					setEditText(binding.editPW2, binding.editPW3, strCurrentValue)
				}
				binding.editPW3.isFocused -> {
					binding.imgLocked3.setImageResource(R.drawable.unlocked_atti)
					setEditText(binding.editPW3, binding.editPW4, strCurrentValue)
				}
				binding.editPW4.isFocused -> {
					binding.imgLocked4.setImageResource(R.drawable.unlocked_atti)
					binding.editPW4.setText(strCurrentValue)
				}
			}
		}

		// 비밀번호를 4자리 모두 입력시
		if (binding.editPW4.text.isNotEmpty() && binding.editPW3.text.isNotEmpty() && binding.editPW2.text.isNotEmpty() && binding.editPW1.text.isNotEmpty()) {
			inputType(intent.getIntExtra("type", 0))
		}
	}

	// 한 칸 지우기를 눌렀을때
	private fun onDeleteKey() {
		when {
			binding.editPW1.isFocused -> {
				binding.editPW1.setText("")
				binding.imgLocked1.setImageResource(R.drawable.locked_atti)
			}
			binding.editPW2.isFocused -> {
				binding.editPW1.setText("")
				binding.editPW1.requestFocus()
				binding.imgLocked1.setImageResource(R.drawable.locked_atti)
			}
			binding.editPW3.isFocused -> {
				binding.editPW2.setText("")
				binding.editPW2.requestFocus()
				binding.imgLocked2.setImageResource(R.drawable.locked_atti)
			}
			binding.editPW4.isFocused -> {
				binding.imgLocked3.setImageResource(R.drawable.locked_atti)
				binding.editPW3.setText("")
				binding.editPW3.requestFocus()
			}
		}
	}

	// 모두 지우기
	private fun onClear(){
		binding.editPW1.setText("")
		binding.editPW2.setText("")
		binding.editPW3.setText("")
		binding.editPW4.setText("")
		binding.editPW1.requestFocus()
		binding.imgLocked1.setImageResource(R.drawable.locked_atti)
		binding.imgLocked2.setImageResource(R.drawable.locked_atti)
		binding.imgLocked3.setImageResource(R.drawable.locked_atti)
		binding.imgLocked4.setImageResource(R.drawable.locked_atti)

	}

	// 입력된 비밀번호를 합치기
	private fun inputedPassword():String {
		return "${binding.editPW1.text}${binding.editPW2.text}${binding.editPW3.text}${binding.editPW4.text}"
	}

	// EditText 설정
	private fun setEditText(currentEditText : EditText, nextEditText: EditText, strCurrentValue : String){
		currentEditText.setText(strCurrentValue)
		nextEditText.requestFocus()
		nextEditText.setText("")
	}

	override fun onBackPressed() {

	}

	// Intent Type 분류
	private fun inputType(type : Int){
		when(type){
			AppLockConst.AppLockCosnt.ENABLE_PASSLOCK ->{ // 잠금설정
				if(oldPwd.isEmpty()){
					oldPwd = inputedPassword()
					onClear()
					binding.textInfoPW.text=getString(R.string.verify_pw_info)
				}
				else{
					if(oldPwd == inputedPassword()){
						AppLock(this).setPassLock(inputedPassword())
						setResult(Activity.RESULT_OK)
						finish()
					}
					else{
						onClear()
						binding.textInfoPW.text = getString(R.string.warning_pw_info)
					}
				}
			}

			AppLockConst.AppLockCosnt.DISABLE_PASSLOCK ->{ // 잠금삭제
				if(AppLock(this).isPassLockSet()){
					if(AppLock(this).checkPassLock(inputedPassword())) {
						AppLock(this).removePassLock()
						setResult(Activity.RESULT_OK)
						finish()
					}
					else {
						binding.textInfoPW.text = getString(R.string.warning_pw_info)
						onClear()
					}
				}
				else{
					setResult(Activity.RESULT_CANCELED)
					finish()
				}
			}

			AppLockConst.AppLockCosnt.UNLOCK_PASSWORD ->
				if(AppLock(this).checkPassLock(inputedPassword())) {
					setResult(Activity.RESULT_OK)
					finish()
				}else{
					binding.textInfoPW.text = getString(R.string.warning_pw_info)
					onClear()
				}

			AppLockConst.AppLockCosnt.CHANGE_PASSWORD -> { // 비밀번호 변경
				if (AppLock(this).checkPassLock(inputedPassword()) && !changePwdUnlock) {
					onClear()
					changePwdUnlock = true
					binding.textInfoPW.text = getString(R.string.new_pw_info)
				}
				else if (changePwdUnlock) {
					if (oldPwd.isEmpty()) {
						oldPwd = inputedPassword()
						onClear()
						binding.textInfoPW.text = getString(R.string.verify_pw_info)
					} else {
						if (oldPwd == inputedPassword()) {
							AppLock(this).setPassLock(inputedPassword())
							setResult(Activity.RESULT_OK)
							finish()
						} else {
							onClear()
							binding.textInfoPW.text = getString(R.string.warning_pw_info)
						}
					}
				} else {
					binding.textInfoPW.text = getString(R.string.warning_pw_info)
					changePwdUnlock = false
					onClear()
				}
			}
		}
	}
}