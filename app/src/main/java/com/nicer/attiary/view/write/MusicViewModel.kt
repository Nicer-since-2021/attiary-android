package com.nicer.attiary.view.write

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MusicViewModel : ViewModel() {

	private val selectedEmotion = MutableLiveData<Int>()

	val getEmotion: LiveData<Int> get() = selectedEmotion

	fun setEmotion(item: Int) {
		selectedEmotion.value = item
	}
}