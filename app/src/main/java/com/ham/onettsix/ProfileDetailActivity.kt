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
import com.ham.onettsix.utils.ProfileImageUtil
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.LoginViewModel
import com.ham.onettsix.viewmodel.MyProfileViewModel
import com.ham.onettsix.viewmodel.ProfileDetailViewModel
import kotlinx.android.synthetic.main.activity_profile_detail.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileDetailActivity : AppCompatActivity(R.layout.activity_profile_detail),
    View.OnClickListener {

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
        profileDetailViewModel.getUserInfo()
        setupObserver()
        login_toolbar_back.setOnClickListener(this)
        profile_detail_eula.setOnClickListener(this)
        profile_detail_eula_forward_img.setOnClickListener(this)
        profile_detail_logout.setOnClickListener(this)
        profile_detail_logout_img.setOnClickListener(this)
    }

    private fun setupObserver() {
        profileDetailViewModel.userInfo.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        profile_detail_version.text = BuildConfig.VERSION_NAME
                        profile_detail_user_name_tv.text = it.data.nickName
                        profile_detail_user_id_tv.text = "#${it.data.id}"
                        profile_detail_image_view.setImageResource(
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
            login_toolbar_back -> {
                finish()
            }
            profile_detail_eula,
            profile_detail_eula_forward_img -> {
                activityResult.launch(Intent(this, EulaActivity::class.java))
            }

            profile_detail_logout,
            profile_detail_logout_img -> {
                profileDetailViewModel.logout()
            }
        }
    }

    private fun updateUI() {
    }
}
