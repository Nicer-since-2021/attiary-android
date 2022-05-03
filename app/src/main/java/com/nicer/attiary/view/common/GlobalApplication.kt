package com.nicer.attiary.view.common

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import com.kakao.sdk.common.KakaoSdk
import com.nicer.attiary.R

class GlobalApplication : Application() {
	companion object {
		var appContext: Context? = null
	}

	override fun onCreate() {
		super.onCreate()

		ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())
	}
}