package com.nicer.attiary.view.signature

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.annotation.Nullable
import com.nicer.attiary.R
import com.nicer.attiary.view.common.GlobalApplication


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
        var sigCheck = GlobalApplication.musicPrefs.getString("sigMusic", "")
        if (sigCheck == "sON") {
            mp?.start()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        mp?.stop()
        mp?.release()
        mp = null
        super.onDestroy()
    }
}