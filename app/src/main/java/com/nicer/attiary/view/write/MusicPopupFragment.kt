package com.nicer.attiary.view.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nicer.attiary.databinding.FragmentMusicPopupBinding

class MusicPopupFragment : Fragment() {
	lateinit var binding: FragmentMusicPopupBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentMusicPopupBinding.inflate(inflater, container, false)

		binding.btnClose.setOnClickListener {
			fragmentManager?.beginTransaction()?.remove(this)?.commit()
		}
		return binding.root
	}
}