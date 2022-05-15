package com.nicer.attiary.view.common

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.nicer.attiary.data.setting.Setting

class GlobalApplication : Application() {
	companion object {
		lateinit var settingPrefs: Setting
	}

	override fun onCreate() {
		super.onCreate()
		settingPrefs = Setting(applicationContext)
		ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())
	}
}