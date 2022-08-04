package com.ham.onettsix

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ham.onettsix.constant.ActivityResultKey
import com.ham.onettsix.constant.ExtraKey
import com.ham.onettsix.fragment.EulaFragment
import kotlinx.android.synthetic.main.activity_permission.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class PermissionActivity : AppCompatActivity(R.layout.activity_permission), View.OnClickListener {

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
                        checkbox_terms.isChecked =
                            it.getBooleanExtra(EulaActivity.RESULT_KEY_CHECK_TERMS, false)
                        checkbox_privacy_policy.isChecked =
                            it.getBooleanExtra(EulaActivity.RESULT_KEY_PRIVACY_POLICY, false)
                        btn_permission_confirm.isEnabled =
                            (checkbox_terms.isChecked && checkbox_privacy_policy.isChecked)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view_eula_item_terms.setOnClickListener(this)
        signup_toolbar_back.back_btn.setOnClickListener(this)
        setupObserver()
    }

    private fun setupObserver() {
    }

    override fun onClick(view: View) {
        var clickedSection = EulaFragment.SECTION_NUMBER_0
        when (view) {
            view_eula_item_terms -> {
                clickedSection = EulaFragment.SECTION_NUMBER_0
            }
            view_eula_item_privacy_policy -> {
                clickedSection = EulaFragment.SECTION_NUMBER_1
            }
            signup_toolbar_back.back_btn -> {
                finish()
                return
            }
        }
        Intent(this@PermissionActivity, EulaActivity::class.java).apply {
            this.putExtra(ExtraKey.PERMISSION_CLICK_SECTION, clickedSection)
            this.putExtra(ExtraKey.PERMISSION_CHECKED_TERMS, checkbox_terms.isChecked)
            this.putExtra(
                ExtraKey.PERMISSION_PRIVACY_POLICY,
                checkbox_privacy_policy.isChecked
            )
            activityResult.launch(this)
        }
    }

    private fun updateUI() {
    }
}
