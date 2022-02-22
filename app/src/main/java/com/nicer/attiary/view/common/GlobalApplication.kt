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

		// 카카오 SDK 초기화
		KakaoSdk.init(this, getString(R.string.native_app_key))
		ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())
	}
}