package com.nicer.attiary.view.ready

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.nicer.attiary.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

	lateinit var binding: FragmentSignInBinding
	lateinit var readyActivity: ReadyActivity

	// 카카오톡 로그인 불가해 카카오계정으로 로그인 시
	val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
		if (error != null) {
			Log.e("KakaoLogin", "카카오계정으로 로그인 실패", error)
		} else if (token != null) {
			Log.i("KakaoLogin", "카카오계정으로 로그인 성공 ${token.accessToken}")
		}
	}

	override fun onAttach(context: Context) {
		if (context is ReadyActivity) readyActivity = context
		super.onAttach(context)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		binding = FragmentSignInBinding.inflate(inflater, container, false)
		return binding.root
	}
}