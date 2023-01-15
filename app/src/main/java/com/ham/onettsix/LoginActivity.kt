package com.ham.onettsix

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.constant.ActivityResultKey
import com.ham.onettsix.constant.ExtraKey
import com.ham.onettsix.constant.ResultCode.PERMISSION_EULA_CONFIRM
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.ActivityLoginBinding
import com.ham.onettsix.dialog.ProgressDialog
import com.ham.onettsix.social.ISocialLoginListener
import com.ham.onettsix.social.KakaoSignInService
import com.ham.onettsix.social.NaverSignInService
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog.getInstance(supportFragmentManager)
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                PERMISSION_EULA_CONFIRM -> {
                    val enableAlarm =
                        result.data?.extras?.getBoolean(ExtraKey.PERMISSION_ALARM_SWITCH) == true
                    this.loginViewModel.signIn.value?.data?.data?.let {
                        loginViewModel.signup(it.socialType, it.socialAccessToken, enableAlarm)
                    }
                }
            }
        }

    private val loginViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
            )
        )[LoginViewModel::class.java]
    }

    private fun loginKakao() {
        progressDialog.show()
        val kakaoService = KakaoSignInService(this)
        kakaoService.signIn(object : ISocialLoginListener {
            override fun getToken(socialType: String, token: String) {
                progressDialog.dismiss()
                loginViewModel.signIn(
                    socialType,
                    token,
                    NotificationManagerCompat.from(this@LoginActivity).areNotificationsEnabled()
                )
            }

            override fun onError(socialType: String) {
                progressDialog.dismiss()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObserve()
        binding.btnMoveEula.setOnClickListener {
            Intent(this, EulaActivity::class.java).apply {
                this.putExtra(ExtraKey.PERMISSION_CLICK_SECTION, 0)
                activityResult.launch(this)
            }
        }
        binding.btnMovePersonalPrivacy.setOnClickListener {
            Intent(this, EulaActivity::class.java).apply {
                this.putExtra(ExtraKey.PERMISSION_CLICK_SECTION, 1)
                activityResult.launch(this)
            }
        }
        binding.loginToolbarBack.setOnClickListener {
            finish()
        }
        binding.btnLoginKakao.setOnClickListener {
            loginKakao()
        }

        binding.btnLoginNaver.setOnClickListener {
            progressDialog.show()
            val naverService = NaverSignInService(this)
            naverService.signIn(object : ISocialLoginListener {
                override fun getToken(socialType: String, token: String) {
                    progressDialog.dismiss()
                    loginViewModel.signIn(
                        socialType,
                        token,
                        NotificationManagerCompat.from(this@LoginActivity).areNotificationsEnabled()
                    )
                }

                override fun onError(socialType: String) {
                    progressDialog.dismiss()
                }
            })
        }
    }

    private fun setupObserve() {
        loginViewModel.signIn.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    if (it.data?.data?.needSignUp == true) {
                        if (it.data.data.socialType == KakaoSignInService.SOCIAL_TYPE_KAKAO) {
                            activityResult.launch(
                                Intent(
                                    this@LoginActivity,
                                    PermissionActivity::class.java
                                )
                            )
                        } else {
                            loginViewModel.signup(
                                it.data.data.socialType,
                                it.data.data.socialAccessToken,
                                NotificationManagerCompat.from(this@LoginActivity)
                                    .areNotificationsEnabled()
                            )
                        }
                    } else {
                        setResult(ActivityResultKey.LOGIN_RESULT_OK)
                        finish()
                    }
                }

                Status.LOADING -> {
                    progressDialog.show()
                }

                Status.ERROR -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, R.string.sign_in_error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}