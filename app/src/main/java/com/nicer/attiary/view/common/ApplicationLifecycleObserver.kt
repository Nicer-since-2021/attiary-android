package com.nicer.attiary.view.common

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.nicer.attiary.data.password.AppLock

class ApplicationLifecycleObserver : DefaultLifecycleObserver {
	companion object {
		const val TAG = "[DEBUG]"
	}

	override fun onStart(owner: LifecycleOwner) {
		super.onStart(owner)
		Log.d(TAG, "onForeground")
	}

	override fun onStop(owner: LifecycleOwner) {
		super.onStop(owner)
		Log.d(TAG, "onBackground")
		AppLock.AppLockStatus.lock = true // 잠금으로 변경

	}
}