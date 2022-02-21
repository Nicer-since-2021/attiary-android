package com.nicer.attiary.view.ready

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.nicer.attiary.databinding.ActivitySignInBinding
import com.nicer.attiary.view.main.HomeActivity

const val RC_SIGN_IN = 1886 // 로그인 인텐트 식별을 위한 임의 상수

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


		// Google Signin
		// 앱에 필요한 사용자 데이터를 요청하도록 로그인 옵션을 설정한다.
		// DEFAULT_SIGN_IN은 유저의 ID와 기본적인 프로필 정보를 요청하는데 사용된다.
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestEmail()
			.build()

		// 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체 생성
		val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

		// 사용자가 이미 Google을 통해 앱에 로그인했는지 확인
		// GoogleSignInAccount가 null이 아닌 객체를 반환하는 경우 사용자는 이미 Google을 통해 앱에 로그인한 것
		// GoogleSignIn.getLastSignedInAccount 가 null 을 반환하면 사용자는 아직 Google을 통해 앱에 로그인하지 않은 것
		val account = GoogleSignIn.getLastSignedInAccount(this)
		// updateUI(account);

		// 로그인 intent 생성
		binding.btnGoogleSignin.setOnClickListener {
			val signInIntent = mGoogleSignInClient.signInIntent
			startActivityForResult(signInIntent, RC_SIGN_IN)
		}
	}

	// 로그인 후에, 사용자의 GoogleSignInAccount 객체 가져오기
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		// Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			// The Task returned from this call is always completed, no need to attach a listener.
			val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
			handleSignInResult(task)
		}
	}

	private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
		try {
			val account = completedTask.getResult(ApiException::class.java)

			// Signed in successfully, show authenticated UI.
			// updateUI(account)
			Log.d("구글로그인 결과 : ", "성공!")
			startActivity(Intent(this, HomeActivity::class.java))
		} catch (e: ApiException) {
			// The ApiException status code indicates the detailed failure reason.
			// Please refer to the GoogleSignInStatusCodes class reference for more information.
			Log.d("구글로그인 결과 : ", "signInResult:failed code=" + e.statusCode)
			// updateUI(null)
		}
	}
}
