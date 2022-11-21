package com.ham.onettsix

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.constant.ActivityResultKey
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.social.ISocialLoginListener
import com.ham.onettsix.social.KakaoSignInService
import com.ham.onettsix.social.NaverSignInService
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    private val loginViewModel by lazy {
        ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
            )
        )[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupObserve()
        login_toolbar_back.back_btn.setOnClickListener {
            finish()
        }
        btn_login_kakao.setOnClickListener {
            val kakaoService = KakaoSignInService(this)
            kakaoService.signIn(object : ISocialLoginListener {
                override fun getToken(socialType: String, token: String) {
                    Log.d("jhlee", "kakao getToken : $token")
                    loginViewModel.signIn(socialType, token)

                }

                override fun onError(socialType: String) {
                    Log.d("jhlee", "kakao onError : $socialType")
                }
            })

        }

        btn_login_kakao.setOnClickListener {
            val kakaoService = KakaoSignInService(this@LoginActivity)
            kakaoService.signIn(object : ISocialLoginListener {
                override fun getToken(socialType: String, token: String) {
                    loginViewModel.signIn(socialType, token)
                }

                override fun onError(socialType: String) {
                    Log.d("jhlee", "onError : $socialType")
                }
            })
        }


        btn_login_naver.setOnClickListener {
            val naverService = NaverSignInService(this)
            naverService.signIn(object : ISocialLoginListener {
                override fun getToken(socialType: String, token: String) {
                    loginViewModel.signIn(socialType, token)
                }

                override fun onError(socialType: String) {
                    Log.d("jhlee", "onError : $socialType")
                }
            })
        }
    }

    private fun setupObserve() {
        loginViewModel.signIn.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d("jhlee", "it : $it")
                    setResult(ActivityResultKey.LOGIN_RESULT_OK)
                    finish()
                }
                Status.ERROR -> {
                    Toast.makeText(this, R.string.sign_in_error, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}