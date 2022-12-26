package com.ham.onettsix

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ham.onettsix.constant.ActivityResultKey
import com.ham.onettsix.constant.ExtraKey
import com.ham.onettsix.databinding.ActivityPermissionBinding
import com.ham.onettsix.fragment.EulaFragment

class PermissionActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPermissionBinding
//    private val permissionViewModel by lazy {
//        ViewModelProviders.of(
//            this,
//            ViewModelFactory(
//                ApiHelperImpl(RetrofitBuilder.apiService),
//                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
//                PreferencesHelper.getInstance(applicationContext)
//            )
//        ).get(PermissionViewModel::class.java)
//    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                ActivityResultKey.EULA_RESULT -> {
                    result.data?.let {

                        binding.checkboxTerms.isChecked =
                            it.getBooleanExtra(EulaActivity.RESULT_KEY_CHECK_TERMS, false)

                        binding.checkboxPrivacyPolicy.isChecked =
                            it.getBooleanExtra(EulaActivity.RESULT_KEY_PRIVACY_POLICY, false)
                        binding.btnPermissionConfirm.isEnabled =
                            (binding.checkboxTerms.isChecked && binding.checkboxPrivacyPolicy.isChecked)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        binding.viewEulaItemTerms.setOnClickListener(this)
        binding.permissionToolbarBack.setOnClickListener(this)
        binding.btnPermissionConfirm.setOnClickListener(this)
        setupObserver()
    }

    private fun setupObserver() {
    }

    override fun onClick(view: View) {
        var clickedSection = EulaFragment.SECTION_NUMBER_0
        when (view) {
            binding.viewEulaItemTerms -> {
                clickedSection = EulaFragment.SECTION_NUMBER_0
            }

            binding.viewEulaItemPrivacyPolicy -> {
                clickedSection = EulaFragment.SECTION_NUMBER_1
            }

            binding.permissionToolbarBack -> {
                finish()
                return
            }

            binding.btnPermissionConfirm -> {
                Intent(this@PermissionActivity, SignUpActivity::class.java).apply {
                    activityResult.launch(this)
                }
                return
            }
        }
        Intent(this@PermissionActivity, EulaActivity::class.java).apply {
            this.putExtra(ExtraKey.PERMISSION_CLICK_SECTION, clickedSection)
            this.putExtra(ExtraKey.PERMISSION_CHECKED_TERMS, binding.checkboxTerms.isChecked)
            this.putExtra(
                ExtraKey.PERMISSION_PRIVACY_POLICY,
                binding.checkboxPrivacyPolicy.isChecked
            )
            activityResult.launch(this)
        }
    }

    private fun updateUI() {
    }
}
