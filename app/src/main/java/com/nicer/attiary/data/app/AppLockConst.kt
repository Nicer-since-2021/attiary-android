package com.nicer.attiary.data.app

var lock = true
class AppLockConst {
	object AppLockCosnt{
		val type ="type"
		val ENABLE_PASSLOCK = 1 //잠금설정
		val DISABLE_PASSLOCK = 2 //잠금 비활성화
		val CHANGE_PASSWORD = 3 //암호변경
		val UNLOCK_PASSWORD = 4 //잠금해제
	}
}