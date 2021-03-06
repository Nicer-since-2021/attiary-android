package com.nicer.attiary.view.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.nicer.attiary.databinding.FragmentMusicPopupBinding
import com.nicer.attiary.util.Emotion

class MusicPopupFragment : Fragment() {
    lateinit var binding: FragmentMusicPopupBinding
    private val viewModel: MusicViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicPopupBinding.inflate(inflater, container, false)

        SetPicker()

        viewModel.getEmotion.observe(viewLifecycleOwner, Observer { item ->
            binding.numberPicker.value = item
        })
        return binding.root
    }

    private fun SetPicker() {
        val picker = binding.numberPicker
        val bgm_category = Emotion.getEmotionKrList()
        picker.minValue = 0
        picker.maxValue = bgm_category.size - 1
        picker.displayedValues = bgm_category
        picker.wrapSelectorWheel = true // 순환

        // 변화값 감지
        picker.setOnValueChangedListener { _, _, newVal ->
            viewModel.setEmotion(newVal)
        }
    }
}