package com.nicer.attiary.view.signature

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import com.nicer.attiary.R


class MusicService : Service() {
	var mp: MediaPlayer? = null

	@Nullable
	override fun onBind(intent: Intent?): IBinder? {
		return null
	}

	override fun onCreate() {
		super.onCreate()
		if (mp == null) mp = MediaPlayer.create(this, R.raw.bluedream)
		mp?.isLooping = true

	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		mp?.start()
		Log.d("TAG", "onStartCommand")
		return super.onStartCommand(intent, flags, startId)
	}

	override fun onDestroy() {
		mp?.stop()
		Log.d("TAG", "onDestroy")
		super.onDestroy()
	}
}