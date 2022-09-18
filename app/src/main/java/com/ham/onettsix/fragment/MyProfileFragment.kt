package com.ham.onettsix.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ham.onettsix.MainActivity
import com.ham.onettsix.ProfileDetailActivity
import com.ham.onettsix.R
import com.ham.onettsix.adapter.MyProfileHistoryAdapter
import com.ham.onettsix.constant.ActivityResultKey
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.dialog.ProgressDialog
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

    private val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                ActivityResultKey.LOGOUT_RESULT_OK -> {
                    (requireActivity() as MainActivity).apply {
                        selectedItem(0)
                        mainViewModel.updateUserInfo()
                    }
                }
            }
        }

    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog.getInstance(parentFragmentManager)
    }

    private lateinit var adapter: MyProfileHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserve()
        adapter = MyProfileHistoryAdapter(requireContext())
        myProfileViewModel.getUserInfo()
        myProfileViewModel.getHistoryInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile_history_rv.adapter = adapter
        profile_history_rv.layoutManager = LinearLayoutManager(requireContext())
        profile_detail_img.setOnClickListener(this@MyProfileFragment)
        profile_image_view.setOnClickListener(this@MyProfileFragment)
        profile_user_name_tv.setOnClickListener(this@MyProfileFragment)
    }

    private fun setupObserve() {
        myProfileViewModel.historyInfo.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    it.data?.data?.let { historyInfo ->
                        adapter.setList(historyInfo)
                    }
                    adapter.notifyDataSetChanged()
                }
                Status.LOADING -> {
                    progressDialog.show()
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                }
            }
        }
        myProfileViewModel.userInfo.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    if (it.data != null) {
                        profile_user_name_tv.text = it.data.nickName
                        profile_user_id_tv.text = "#${it.data.id}"
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
                    progressDialog.dismiss()
                }
                Status.LOADING -> {
                    progressDialog.show()
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
            profile_detail_img,
            profile_image_view,
            profile_user_name_tv -> {
                result.launch(Intent(requireContext(), ProfileDetailActivity::class.java))
            }
        }
    }
}