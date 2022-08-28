package com.ham.onettsix.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ham.onettsix.MainActivity
import com.ham.onettsix.R
import com.ham.onettsix.adapter.MyProfileHistoryAdapter
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
        profile_user_logout_title.setOnClickListener(this@MyProfileFragment)
        profile_logout_img.setOnClickListener(this@MyProfileFragment)
    }

    private fun setupObserve() {
        myProfileViewModel.historyInfo.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    it.data?.let { historyInfo ->
                        adapter.setList(historyInfo.data)
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
            profile_user_logout_title, profile_logout_img -> {
                myProfileViewModel.logout()
            }
        }
    }
}