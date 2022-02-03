package com.nicer.attiary.data.app
import android.content.Context

class AppLock(context: Context) {

	private var sharedPref = context.getSharedPreferences("appLock", Context.MODE_PRIVATE)

	// 잠금 설정
	fun setPassLock(password : String){
		sharedPref.edit().apply{
			putString("applock", password)
			apply()
		}
	}

	// 잠금 설정 제거
	fun removePassLock(){
		sharedPref.edit().apply{
			remove("applock")
			apply()
		}
	}

	// 입력한 비밀번호가 맞는가?
	fun checkPassLock(password: String): Boolean {
		return sharedPref.getString("applock","0") == password
	}

	// 잠금 설정이 되어있는가?
	fun isPassLockSet(): Boolean {
		if(sharedPref.contains("applock")){
			return true
		}
		return false
	}
}