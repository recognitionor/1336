package com.ham.onettsix.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.MainActivity
import com.ham.onettsix.R
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.utils.ProfileImageUtil
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.MyProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class MyProfileFragment : Fragment(), View.OnClickListener {

    companion object {
        const val TAG = "MyProfileFragment"
    }

    private val myProfileViewModel by lazy {
        ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[MyProfileViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserve()
        myProfileViewModel.getUserInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile_user_logout_title.setOnClickListener(this@MyProfileFragment)
        profile_logout_img.setOnClickListener(this@MyProfileFragment)
    }

    private fun setupObserve() {
        myProfileViewModel.userInfo.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        profile_user_name_tv.text = it.data.nickName
                        profile_image_view.setImageResource(
                            ProfileImageUtil.getImageId(
                                it.data.profileImageId ?: -1
                            )
                        )
                    } else {
                        (requireActivity() as MainActivity).apply {
                            selectedItem(0)
                            mainViewModel.updateUserInfo()
                        }
                    }
                }
                Status.ERROR -> {

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, null)
    }

    override fun onClick(v: View?) {
        when (v) {
            profile_user_logout_title, profile_logout_img -> {
                myProfileViewModel.logout()
            }
        }
    }
}