package com.nicer.attiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nicer.attiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.btnSay.setOnClickListener {
			binding.textSay.text = "Hello, Kotlin!"
		}
	}
}