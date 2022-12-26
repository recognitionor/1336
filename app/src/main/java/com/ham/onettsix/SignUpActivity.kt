package com.ham.onettsix

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ham.onettsix.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignUpBinding

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
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObserver()
        binding.signupToolbarBack.setOnClickListener {
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
