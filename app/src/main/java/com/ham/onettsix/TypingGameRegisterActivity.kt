package com.ham.onettsix

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ham.onettsix.adapter.TypingGameTagAdapter
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.ActivityTypingRegisterBinding
import com.ham.onettsix.dialog.EditTextDialog
import com.ham.onettsix.dialog.OneButtonDialog
import com.ham.onettsix.dialog.ProgressDialog
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.TypingGameRegisterViewModel

class TypingGameRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTypingRegisterBinding

    private lateinit var tagAdapter: TypingGameTagAdapter

    private lateinit var progressDialog: ProgressDialog

    private val typingGameRegisterViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
            )
        )[TypingGameRegisterViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tagAdapter = TypingGameTagAdapter()
        binding = ActivityTypingRegisterBinding.inflate(layoutInflater)
        progressDialog = ProgressDialog()
        setContentView(binding.root)
        setupObserver()

        binding.typingGameRegisterTagRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.typingGameRegisterTagRv.adapter = tagAdapter
        binding.typingGameRegisterBtn.setOnClickListener {

            typingGameRegisterViewModel.registerTypingGame(
                binding.typingGameRegisterContentEdit.text.toString(),
                binding.typingGameRegisterDescriptionEdit.text.toString(),
                tagAdapter.getSelectedList()
            )
        }
        binding.typingGameRegisterTagBtn.setOnClickListener {
            val editTextDialog = EditTextDialog("태그를 입력 해주세요") { dialog, input ->
                dialog.dismiss()
                tagAdapter.addTag(input)
                tagAdapter.notifyDataSetChanged()
            }.show(supportFragmentManager, EditTextDialog.TAG)
        }
        binding.typingGameRegisterToolbarBack.setOnClickListener {
            finish()
        }
    }

    private fun setupObserver() {
        typingGameRegisterViewModel.registerResult.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dismiss()
                    OneButtonDialog(getString(R.string.typing_game_register_success), "") {
                        finish()
                    }.show(
                        supportFragmentManager, OneButtonDialog.TAG
                    )
                }

                Status.LOADING -> {
                    progressDialog.show()
                }

                Status.ERROR -> {
                    progressDialog.dismiss()
                    Toast.makeText(this, getString(R.string.typing_game_register_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
        typingGameRegisterViewModel.tagList.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    tagAdapter.setList(it.data)
                    tagAdapter.notifyDataSetChanged()
                }

                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }
}