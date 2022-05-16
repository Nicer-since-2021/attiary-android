package com.nicer.attiary.view.signature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.nicer.attiary.R

import com.nicer.attiary.databinding.FragmentLoadingBinding
import com.nicer.attiary.databinding.FragmentSetEmotionBinding

class SetEmotionFragment : Fragment() {
	lateinit var binding: FragmentSetEmotionBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentSetEmotionBinding.inflate(inflater, container, false)

		binding.buttonBack.setOnClickListener {
			activity?.supportFragmentManager
				?.beginTransaction()
				?.remove(this)
				?.commit()
		}

		val buttonArray = arrayListOf<ImageButton>(binding.anger1, binding.anger2, binding.anger3, binding.anxiety1, binding.anxiety2, binding.anxiety3, binding.hope1, binding.hope2, binding.hope3, binding.joy1, binding.joy2, binding.joy3, binding.neutrality, binding.regret1, binding.regret2, binding.regret3, binding.sadness1, binding.sadness2, binding.sadness3, binding.tiredness1, binding.tiredness2, binding.tiredness3)
		for (button in buttonArray){
			button.setOnClickListener(btnListener)
		}
		return binding.root
	}

	private val btnListener = View.OnClickListener { view ->
		var emotion = ""
		when (view.id) {
			R.id.anger1 -> emotion = "anger1"
			R.id.anger2 -> emotion = "anger2"
			R.id.anger3 -> emotion = "anger3"
			R.id.anxiety1 -> emotion = "anxiety1"
			R.id.anxiety2 -> emotion = "anxiety2"
			R.id.anxiety3 -> emotion = "anxiety3"
			R.id.hope1 -> emotion = "hope1"
			R.id.hope2 -> emotion = "hope2"
			R.id.hope3 -> emotion = "hope3"
			R.id.joy1 -> emotion = "joy1"
			R.id.joy2 -> emotion = "joy2"
			R.id.joy3 -> emotion = "joy3"
			R.id.neutrality -> emotion = "neutrality"
			R.id.regret1 -> emotion = "regret1"
			R.id.regret2 -> emotion = "regret2"
			R.id.regret3 -> emotion = "regret3"
			R.id.sadness1 -> emotion = "sadness1"
			R.id.sadness2 -> emotion = "sadness2"
			R.id.sadness3 -> emotion = "sadness3"
			R.id.tiredness1 -> emotion = "tiredness1"
			R.id.tiredness2 -> emotion = "tiredness2"
			R.id.tiredness3 -> emotion = "tiredness3"
		}
		setFragmentResult("requestKey", bundleOf("bundleKey" to emotion))
		activity?.supportFragmentManager
			?.beginTransaction()
			?.remove(this)
			?.commit()
	}
}