package com.nicer.attiary.view.signature

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nicer.attiary.databinding.FragmentErrorBinding

class ErrorFragment : Fragment() {
	lateinit var binding: FragmentErrorBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentErrorBinding.inflate(inflater, container, false)

		binding.btn.setOnClickListener {
			activity?.supportFragmentManager
				?.beginTransaction()
				?.remove(this)
				?.commit()
		}

		return binding.root
	}
}