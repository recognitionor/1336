package com.ham.onettsix

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_APPLICATION_SETTINGS
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.constant.ActivityResultKey.LOGOUT_RESULT_OK
import com.ham.onettsix.constant.ExtraKey
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.ActivityProfileDetailBinding
import com.ham.onettsix.utils.ProfileImageUtil
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.ProfileDetailViewModel

class ProfileDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityProfileDetailBinding

    private val profileDetailViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
            )
        )[ProfileDetailViewModel::class.java]
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            NotificationManagerCompat.from(this).areNotificationsEnabled().let {
                binding.profileDetailEventSwitch.isChecked = it
                profileDetailViewModel.changeAlarm(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileDetailViewModel.getUserInfo()
        setupObserver()

        binding.profileDetailEventSwitch.setOnClickListener {
            binding.profileDetailEventSwitch.isChecked = NotificationManagerCompat.from(this).areNotificationsEnabled()
            activityResult.launch(
                Intent(
                    ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                )
            )
        }

        NotificationManagerCompat.from(this).areNotificationsEnabled().let {
            binding.profileDetailEventSwitch.isChecked = it
            profileDetailViewModel.changeAlarm(it)
        }

        binding.profileDetailNoticeImg.setOnClickListener(this)
        binding.profileDetailNotice.setOnClickListener(this)
        binding.loginToolbarBack.setOnClickListener(this)
        binding.profileDetailEula.setOnClickListener(this)
        binding.profileDetailEulaForwardImg.setOnClickListener(this)
        binding.profileDetailLogout.setOnClickListener(this)
        binding.profileDetailLogoutImg.setOnClickListener(this)
        binding.profileDetailWithdraw.setOnClickListener(this)
    }

    private fun setupObserver() {
        profileDetailViewModel.withDraw.observe(this) {
            setResult(LOGOUT_RESULT_OK)
            finish()
        }
        profileDetailViewModel.userInfo.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        binding.profileDetailVersion.text = BuildConfig.VERSION_NAME
                        binding.profileDetailUserNameTv.text = it.data.nickName
                        binding.profileDetailUserIdTv.text = "#${it.data.id}"
                        binding.profileDetailImageView.setImageResource(
                            ProfileImageUtil.getImageId(
                                it.data.profileImageId ?: -1
                            )
                        )
                    } else {
                        setResult(LOGOUT_RESULT_OK)
                        finish()
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
            }
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding.profileDetailNotice, binding.profileDetailNoticeImg -> {
                activityResult.launch(Intent(this, NoticeActivity::class.java))
            }
            binding.profileDetailWithdraw -> {
                Log.d("jhlee", "profileDetailViewModel.withDraw()")
                profileDetailViewModel.withDraw()
            }
            binding.loginToolbarBack -> {
                finish()
            }
            binding.profileDetailEula, binding.profileDetailEulaForwardImg -> {
                activityResult.launch(Intent(this, EulaActivity::class.java))
            }
            binding.profileDetailLogout, binding.profileDetailLogoutImg -> {
                profileDetailViewModel.logout()

            }
        }
    }

    private fun updateUI() {
    }
}
