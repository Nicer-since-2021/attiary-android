package com.nicer.attiary.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.databinding.ActivityReadyBinding

class MainActivity2 : AppCompatActivity() {
	val binding by lazy { ActivityReadyBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
	}
}