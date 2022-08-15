package com.ham.onettsix

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(R.layout.activity_sign_up), View.OnClickListener {

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
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserver()
        signup_toolbar_back.setOnClickListener {
            finish()
        }
    }

    private fun setupObserver() {
    }

    override fun onClick(view: View) {
    }

    private fun updateUI() {
    }
}
