package com.nicer.attiary

import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.nicer.attiary.data.app.AppLock
import com.nicer.attiary.data.app.AppLockConst
import com.nicer.attiary.data.app.lock
import com.nicer.attiary.view.AppPassWordActivity

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