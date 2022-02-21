package com.nicer.attiary.view.signature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.databinding.ActivityMonthlyReportBinding

// 추후에 지워야함!!!
class MonthlyReportActivity : AppCompatActivity() {

	private val binding by lazy { ActivityMonthlyReportBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
	}
}