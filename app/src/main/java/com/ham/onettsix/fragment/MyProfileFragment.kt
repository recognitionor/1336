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
import com.ham.onettsix.data.model.HistoryInfo
import com.ham.onettsix.databinding.FragmentProfileBinding
import com.ham.onettsix.dialog.ProgressDialog
import com.ham.onettsix.utils.ProfileImageUtil
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.MyProfileViewModel

class MyProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentProfileBinding

    companion object {
        val TAG = MyProfileFragment::class.simpleName
    }

    private val myProfileViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
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
                        RetrofitBuilder.accessToken = ""
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
        adapter = MyProfileHistoryAdapter(
            requireContext(),
            object : MyProfileHistoryAdapter.OnItemWinningListener {
                override fun onWinnerClick(item: HistoryInfo.Data) {
                    myProfileViewModel.getWinnerSecretCode(item.episode)
                }
            })
        myProfileViewModel.getUserInfo()
        myProfileViewModel.getHistoryInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileHistoryRv.adapter = adapter
        binding.profileHistoryRv.layoutManager = LinearLayoutManager(requireContext())
        binding.profileDetailImg.setOnClickListener(this@MyProfileFragment)
        binding.profileImageView.setOnClickListener(this@MyProfileFragment)
        binding.profileUserNameTv.setOnClickListener(this@MyProfileFragment)
    }

    private fun setupObserve() {
        myProfileViewModel.winnerSecretCode.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val email = Intent(Intent.ACTION_SEND)
                    email.putExtra(Intent.EXTRA_USER, arrayOf("ham.factories@gmail.com"))
                    email.putExtra(Intent.EXTRA_EMAIL, arrayOf("ham.factories@gmail.com"))
                    email.putExtra(Intent.EXTRA_SUBJECT, "환급신청")
                    email.putExtra(
                        Intent.EXTRA_TEXT,
                        getString(
                            R.string.mail_content,
                            it.data?.data?.episode ?: 0,
                            it.data?.data?.secretCode ?: 0
                        )
                    )

                    email.type = "message/rfc822"

                    binding.root.context.startActivity(
                        Intent.createChooser(
                            email, "Choose an Email client :"
                        )
                    )

                }

                Status.LOADING -> {

                }

                Status.ERROR -> {

                }
            }
        }
        myProfileViewModel.historyInfo.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    it.data?.data?.let { historyInfo ->
                        if (historyInfo.isEmpty()) {
                            binding.rvItemHeaderMyProfileHistoryInclude.rvItemHeaderMyProfileHistory.visibility =
                                View.GONE
                            binding.profileHistoryRvEmpty.visibility = View.VISIBLE
                            binding.profileHistoryRv.visibility = View.GONE
                        } else {
                            binding.rvItemHeaderMyProfileHistoryInclude.rvItemHeaderMyProfileHistory.visibility =
                                View.VISIBLE
                            binding.profileHistoryRvEmpty.visibility = View.GONE
                            binding.profileHistoryRv.visibility = View.VISIBLE
                            adapter.setList(historyInfo)
                            adapter.notifyDataSetChanged()
                        }
                    }

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
                        binding.profileUserNameTv.text = it.data.nickName
                        binding.profileUserIdTv.text = "#${it.data.id}"
                        binding.profileImageView.setImageResource(
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onClick(v: View?) {
        when (v) {

            binding.profileDetailImg, binding.profileImageView, binding.profileUserNameTv -> {
                result.launch(Intent(requireContext(), ProfileDetailActivity::class.java))
            }
        }
    }
}