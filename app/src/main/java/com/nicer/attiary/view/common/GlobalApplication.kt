package com.nicer.attiary.view.common

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.nicer.attiary.data.setting.MusicSwitch

class GlobalApplication : Application() {
	companion object {
		lateinit var musicPrefs: MusicSwitch
	}

	override fun onCreate() {
		super.onCreate()
		musicPrefs = MusicSwitch(applicationContext)
		ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())
	}
}