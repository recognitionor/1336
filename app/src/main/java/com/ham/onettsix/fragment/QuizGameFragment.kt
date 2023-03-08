package com.ham.onettsix.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.QuizGameActivity
import com.ham.onettsix.constant.ExtraKey
import com.ham.onettsix.constant.ResultCode
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.FragmentQuizGameBinding
import com.ham.onettsix.dialog.DialogDismissListener
import com.ham.onettsix.dialog.QuizTagSelectDialog
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.QuizViewModel

class QuizGameFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentQuizGameBinding

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {

            }
        }

    private val quizTagSelectDialog by lazy {
        QuizTagSelectDialog(object : DialogDismissListener {
            override fun onDismissListener() {
                Log.d("jhlee", "dismiss")
                activityResult.launch(Intent(requireContext(), QuizGameActivity::class.java))
            }
        })
    }

    private val quizGameViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[QuizViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("jhlee", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizGameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserve();
        if (PreferencesHelper.getInstance(requireContext()).isLogin()) {
            binding.quizFragmentLayout.visibility = View.VISIBLE
            binding.layoutQuizNeededLogin.layoutGameNeededLogin.visibility = View.GONE
        } else {
            binding.quizFragmentLayout.visibility = View.GONE
            binding.layoutQuizNeededLogin.layoutGameNeededLogin.visibility = View.VISIBLE
        }
        binding.layoutQuizNeededLogin.rpsGameLoginBtn.setOnClickListener(this@QuizGameFragment)
        binding.quizGoBtn.setOnClickListener(this@QuizGameFragment)
        binding.quizGoImg.setOnClickListener(this@QuizGameFragment)
    }

    private fun setupObserve() {
        quizGameViewModel.quizTagList.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { list ->
                        quizTagSelectDialog.quizTagAdapter.setItemList(list)
                        quizTagSelectDialog.quizTagAdapter.notifyDataSetChanged()
                    }
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.layoutQuizNeededLogin.rpsGameLoginBtn -> {
                (parentFragment as GameFragment).login()
            }
            binding.quizGoBtn, binding.quizGoImg -> {
                quizGameViewModel.getQuizTag()
                quizTagSelectDialog.show(childFragmentManager, "test")
            }
        }
    }
}
