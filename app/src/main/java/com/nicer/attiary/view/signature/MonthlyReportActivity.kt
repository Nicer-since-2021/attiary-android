package com.nicer.attiary.view.signature

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.data.password.AppLock
import com.nicer.attiary.databinding.ActivityMonthlyReportBinding
import com.nicer.attiary.view.common.AppPassWordActivity

// 추후에 지워야함!!!
class MonthlyReportActivity : AppCompatActivity() {

	private val binding by lazy { ActivityMonthlyReportBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
	}
	override fun onResume() {
		super.onResume()
		if (AppLock.AppLockStatus.lock && AppLock(this).isPassLockSet()) {
			val intent = Intent(this, AppPassWordActivity::class.java).apply {
				putExtra("type", AppLock.AppLockStatus.UNLOCK_PASSWORD)
			}
			startActivity(intent)
		}
		window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
	}

	override fun onPause() {
		super.onPause()
		window.setFlags(
			WindowManager.LayoutParams.FLAG_SECURE,
			WindowManager.LayoutParams.FLAG_SECURE
		)
	}
}