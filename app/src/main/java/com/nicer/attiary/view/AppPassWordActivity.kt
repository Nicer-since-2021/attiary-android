package com.nicer.attiary.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.R
import com.nicer.attiary.data.app.AppLock
import com.nicer.attiary.data.app.AppLockConst
import kotlinx.android.synthetic.main.activity_app_password.*

class AppPassWordActivity : AppCompatActivity(){
	private var oldPwd =""
	private var changePwdUnlock = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_app_password)

		editPW1.showSoftInputOnFocus = false
		editPW2.showSoftInputOnFocus = false
		editPW3.showSoftInputOnFocus = false
		editPW4.showSoftInputOnFocus = false

		val buttonArray = arrayListOf<Button>(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7 ,btn8, btn9, btnDel)
		for (button in buttonArray){
			button.setOnClickListener(btnListener)
		}
		btn_pwCancel.setOnClickListener {
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
				editPW1.isFocused -> {
					setEditText(editPW1, editPW2, strCurrentValue)
				}
				editPW2.isFocused -> {
					setEditText(editPW2, editPW3, strCurrentValue)
				}
				editPW3.isFocused -> {
					setEditText(editPW3, editPW4, strCurrentValue)
				}
				editPW4.isFocused -> {
					editPW4.setText(strCurrentValue)
				}
			}
		}

		// 비밀번호를 4자리 모두 입력시
		if (editPW4.text.isNotEmpty() && editPW3.text.isNotEmpty() && editPW2.text.isNotEmpty() && editPW1.text.isNotEmpty()) {
			inputType(intent.getIntExtra("type", 0))
		}
	}

	// 한 칸 지우기를 눌렀을때
	private fun onDeleteKey() {
		when {
			editPW1.isFocused -> {
				editPW1.setText("")
			}
			editPW2.isFocused -> {
				editPW1.setText("")
				editPW1.requestFocus()
			}
			editPW3.isFocused -> {
				editPW2.setText("")
				editPW2.requestFocus()
			}
			editPW4.isFocused -> {
				editPW3.setText("")
				editPW3.requestFocus()
			}
		}
	}

	// 모두 지우기
	private fun onClear(){
		editPW1.setText("")
		editPW2.setText("")
		editPW3.setText("")
		editPW4.setText("")
		editPW1.requestFocus()
	}

	// 입력된 비밀번호를 합치기
	private fun inputedPassword():String {
		return "${editPW1.text}${editPW2.text}${editPW3.text}${editPW4.text}"
	}

	// EditText 설정
	private fun setEditText(currentEditText : EditText, nextEditText: EditText, strCurrentValue : String){
		currentEditText.setText(strCurrentValue)
		nextEditText.requestFocus()
		nextEditText.setText("")
	}

	// Intent Type 분류
	private fun inputType(type : Int){
		when(type){
			AppLockConst.AppLockCosnt.ENABLE_PASSLOCK ->{ // 잠금설정
				if(oldPwd.isEmpty()){
					oldPwd = inputedPassword()
					onClear()
					textInfoPW.text=getString(R.string.verify_pw_info)
				}
				else{
					if(oldPwd == inputedPassword()){
						AppLock(this).setPassLock(inputedPassword())
						setResult(Activity.RESULT_OK)
						finish()
					}
					else{
						onClear()
						textInfoPW.text = getString(R.string.warning_pw_info)
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
						textInfoPW.text = getString(R.string.warning_pw_info)
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
					textInfoPW.text = getString(R.string.warning_pw_info)
					onClear()
				}

			AppLockConst.AppLockCosnt.CHANGE_PASSWORD -> { // 비밀번호 변경
				if (AppLock(this).checkPassLock(inputedPassword()) && !changePwdUnlock) {
					onClear()
					changePwdUnlock = true
					textInfoPW.text = getString(R.string.new_pw_info)
				}
				else if (changePwdUnlock) {
					if (oldPwd.isEmpty()) {
						oldPwd = inputedPassword()
						onClear()
						textInfoPW.text = getString(R.string.verify_pw_info)
					} else {
						if (oldPwd == inputedPassword()) {
							AppLock(this).setPassLock(inputedPassword())
							setResult(Activity.RESULT_OK)
							finish()
						} else {
							onClear()
							textInfoPW.text = getString(R.string.warning_pw_info)
						}
					}
				} else {
					textInfoPW.text = getString(R.string.warning_pw_info)
					changePwdUnlock = false
					onClear()
				}
			}
		}
	}
}