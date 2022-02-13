package com.nicer.attiary.view.ready

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nicer.attiary.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {
	lateinit var binding: FragmentSplashBinding
	lateinit var readyActivity: ReadyActivity

	override fun onAttach(context: Context) {
		if (context is ReadyActivity) readyActivity = context
		super.onAttach(context)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentSplashBinding.inflate(inflater, container, false)
		return binding.root
	}
}