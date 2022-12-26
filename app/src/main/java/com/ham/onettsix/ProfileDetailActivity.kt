package com.ham.onettsix

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.constant.ActivityResultKey.LOGOUT_RESULT_OK
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

class ProfileDetailActivity : AppCompatActivity(),
    View.OnClickListener {

    private lateinit var binding: ActivityProfileDetailBinding

    private val profileDetailViewModel by lazy {
        ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
            )
        )[ProfileDetailViewModel::class.java]
    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileDetailViewModel.getUserInfo()
        setupObserver()

        binding.loginToolbarBack.setOnClickListener(this)
        binding.profileDetailEula.setOnClickListener(this)
        binding.profileDetailEulaForwardImg.setOnClickListener(this)
        binding.profileDetailLogout.setOnClickListener(this)
        binding.profileDetailLogoutImg.setOnClickListener(this)
    }

    private fun setupObserver() {
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
            binding.loginToolbarBack -> {
                finish()
            }
            binding.profileDetailEula,
            binding.profileDetailEulaForwardImg -> {
                activityResult.launch(Intent(this, EulaActivity::class.java))
            }
            binding.profileDetailLogout,
            binding.profileDetailLogoutImg -> {
                profileDetailViewModel.logout()
            }
        }
    }

    private fun updateUI() {
    }
}
