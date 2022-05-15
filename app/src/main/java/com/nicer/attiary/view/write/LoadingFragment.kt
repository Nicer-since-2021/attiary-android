package com.nicer.attiary.view.write

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.nicer.attiary.R
import com.nicer.attiary.databinding.FragmentLoadingBinding

class LoadingFragment : Fragment() {
	lateinit var binding: FragmentLoadingBinding

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentLoadingBinding.inflate(inflater, container, false)

		Glide.with(this).load(R.raw.loading_atti).override(500, 500).into(binding.imageView)

		Thread {
			for (i in 0..100) {
				try {
					Thread.sleep(50)
				} catch (e: InterruptedException) {
					e.printStackTrace()
				}
				binding.loadingPB.setProgress(i)
				binding.progressNumber.post(Runnable {
					binding.progressNumber.setText("$i%")
					if (i == 100) {
						binding.progressNumber.setText("완료")
					}
				})
			}
		}.start()

		return binding.root
	}
}