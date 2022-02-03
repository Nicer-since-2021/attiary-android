package com.nicer.attiary.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.nicer.attiary.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

	val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }

	// 카카오톡 로그인 불가해 카카오계정으로 로그인 시
	val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
		if (error != null) {
			Log.e("KakaoLogin", "카카오계정으로 로그인 실패", error)
		} else if (token != null) {
			Log.i("KakaoLogin", "카카오계정으로 로그인 성공 ${token.accessToken}")
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		binding.btnKakaoSignin.setOnClickListener {
			// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인력
			if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
				UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
					if (error != null) {
						Log.e("KakaotalkLogin", "카카오톡으로 로그인 실패", error)

						// 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
						// 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
						if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
							return@loginWithKakaoTalk
						}

						// 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
						UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
					} else if (token != null) {
						Log.i("KakaotalkLogin", "카카오톡으로 로그인 성공 ${token.accessToken}")
					}
				}
			} else {
				UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
			}
		}
	}
}
