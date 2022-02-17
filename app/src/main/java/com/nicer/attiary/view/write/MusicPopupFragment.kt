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

		setPicker()

		binding.btnClose.setOnClickListener {
			fragmentManager?.beginTransaction()?.remove(this)?.commit()
		}
		return binding.root
	}

	private fun setPicker() {
		val picker = binding.numberPicker
		val bgm_category = arrayOf("기쁨", "희망", "슬픔", "분노", "불안", "피곤", "후회")
		picker.minValue = 0
		picker.maxValue = bgm_category.size - 1
		picker.displayedValues = bgm_category
		picker.wrapSelectorWheel = true // 순환

		// 변화값 감지
//		picker.setOnValueChangedListener { picker, oldVal, newVal ->
//			val text = "Changed from $oldVal to $newVal"
//			Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
//		}
	}
}