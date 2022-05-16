package com.nicer.attiary.view.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nicer.attiary.R
import com.nicer.attiary.databinding.FragmentSetChatbotBinding
import com.nicer.attiary.view.common.GlobalApplication

class SetChatbotFragment : Fragment() {
	lateinit var binding: FragmentSetChatbotBinding
	var chatMode = ""

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentSetChatbotBinding.inflate(inflater, container, false)

		chatMode = GlobalApplication.settingPrefs.getString("chatMode", "")
		if (chatMode == "sympathy")
			binding.radioGruop.check(R.id.SympathyRadio)
		else
			binding.radioGruop.check(R.id.consolationRadio)



		binding.radioGruop.setOnCheckedChangeListener { radioGroup, i ->
			when(i){
				R.id.SympathyRadio -> chatMode = "sympathy"
				R.id.consolationRadio -> chatMode = "consolation"
			}
		}

		binding.backBtn.setOnClickListener {
			activity?.supportFragmentManager
				?.beginTransaction()
				?.remove(this)
				?.commit()
		}

		binding.saveBtn.setOnClickListener {
			GlobalApplication.settingPrefs.setString("chatMode", chatMode)
			activity?.supportFragmentManager
				?.beginTransaction()
				?.remove(this)
				?.commit()
		}

		return binding.root
	}
}