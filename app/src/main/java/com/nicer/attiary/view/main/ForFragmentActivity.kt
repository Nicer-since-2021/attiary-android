package com.nicer.attiary.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nicer.attiary.databinding.ActivityForFragmentBinding

class ForFragmentActivity : AppCompatActivity() {
	private val binding by lazy { ActivityForFragmentBinding.inflate(layoutInflater) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
	}
}