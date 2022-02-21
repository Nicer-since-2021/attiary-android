package com.nicer.attiary.util.lock

var lock = true

class AppLockStatus {
	object AppLockStatus {
		val TYPE = "type"
		val ENABLE_PASSLOCK = 1 //잠금설정
		val DISABLE_PASSLOCK = 2 //잠금 비활성화
		val CHANGE_PASSWORD = 3 //암호변경
		val UNLOCK_PASSWORD = 4 //잠금해제
	}
}