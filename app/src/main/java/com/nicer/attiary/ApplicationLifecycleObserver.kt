package com.nicer.attiary

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

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
	}
}