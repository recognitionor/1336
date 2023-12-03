package com.ham.onettsix

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.constant.ExtraKey
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.databinding.ActivityTypingGameBinding
import com.ham.onettsix.dialog.OneButtonDialog
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.TypingGameViewModel

class TypingGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTypingGameBinding

    private var isRankGame = false

    private val typingGameViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                PreferencesHelper.getInstance(applicationContext)
            )
        )[TypingGameViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initIntentExtra()

        binding = ActivityTypingGameBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setupObserver()
        binding.typingGameToolbarBack.setOnClickListener { finish() }
        binding.typingGameStartBtn.setOnClickListener {
            it.visibility = View.GONE
            typingGameViewModel.ready()
        }

        binding.typingGameTypingEditText.setOnEditorActionListener(object :
            TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (binding.typingGameTypingEditText.text.toString() != binding.typingGameQuestionTv.text.toString()) {
                        Toast.makeText(this@TypingGameActivity, "글자가 틀렸습니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        binding.typingGameTypingEditText.clearFocus()
                        val imm =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(binding.typingGameTypingEditText.windowToken, 0)
                        typingGameViewModel.finishGame()
                    }
                    return true
                }
                return false
            }
        })

        binding.typingGameTypingEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (typingGameViewModel.typingGameStatus.value == TypingGameViewModel.GAME_START_STATUS_READY) {
                    if (isRankGame) {
                        typingGameViewModel.startRankGame()
                    } else {
                        typingGameViewModel.startRandomGame()
                    }
                }
                val question = binding.typingGameQuestionTv.text.toString()
                val span = SpannableString(question)
                if ((s?.length ?: 0) < 2) {
                    span.setSpan(
                        ForegroundColorSpan(Color.BLACK),
                        0,
                        question.length,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else {
                    s?.forEachIndexed { index, c ->
                        if (index + 1 < s.length) {
                            val originTemp = question.substring(index, index + 1)
                            if (originTemp == c.toString()) {
                                span.setSpan(
                                    ForegroundColorSpan(Color.GREEN),
                                    index,
                                    index + 1,
                                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            } else {
                                span.setSpan(
                                    ForegroundColorSpan(Color.RED),
                                    index,
                                    index + 1,
                                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }

                        }
                    }
                }
                binding.typingGameQuestionTv.text = span
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun initIntentExtra() {
        val questionId = intent.getLongExtra(ExtraKey.TYPING_GAME_QUESTION_ID, -1)
        val episode = intent.getIntExtra(ExtraKey.TYPING_GAME_EPISODE, -1)
        val content = intent.getStringExtra(ExtraKey.TYPING_GAME_CONTENT) ?: ""
        val isRankGame = intent.getBooleanExtra(ExtraKey.TYPING_GAME_IS_RANK_GAME, false)
        this.isRankGame = isRankGame
        if (questionId > 0 && content.isNotEmpty()) {
            typingGameViewModel.setTypingGameInfo(questionId.toInt(), episode, content)
        } else {
            Toast.makeText(this, getString(R.string.common_error), Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    private fun setupObserver() {
        typingGameViewModel.endTypingGame.observe(this) {
            Log.d("jhlee", "${typingGameViewModel.typingGameTimer.value}")
            OneButtonDialog(
                it.resultMsg, getString(
                    R.string.typing_game_result_content,
                    "${typingGameViewModel.typingGameTimer.value}"
                )
            ) { dialog ->
                finish()
                dialog.dismiss()
            }.show(supportFragmentManager, OneButtonDialog.TAG)
        }
        typingGameViewModel.content.observe(this) {
            binding.typingGameQuestionTv.text = it
        }


        typingGameViewModel.typingGameStatus.observe(this) {
            when (it) {
                TypingGameViewModel.GAME_START_STATUS_ERROR -> {
                    OneButtonDialog(
                        getString(R.string.typing_game_invalid_game)
                    ) { dialog ->
                        dialog.dismiss()
                        this@TypingGameActivity.setResult(Activity.RESULT_OK)
                        this@TypingGameActivity.finish()
                    }.show(supportFragmentManager, OneButtonDialog.TAG)
                }

                TypingGameViewModel.GAME_START_STATUS_DEFAULT -> {}
                TypingGameViewModel.GAME_START_STATUS_READY -> {
                    binding.typingGameTypingEditText.visibility = View.VISIBLE
                    binding.typingGameTypingEditText.requestFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(
                        binding.typingGameTypingEditText, InputMethodManager.SHOW_IMPLICIT
                    )
                }

                TypingGameViewModel.GAME_START_STATUS_ING -> {}
                TypingGameViewModel.GAME_START_STATUS_DONE -> {
//                    OneButtonDialog(
//                        "it.resultMsg", getString(
//                            R.string.typing_game_result_content,
//                            "${typingGameViewModel.typingGameTimer.value}"
//                        )
//                    ) { dialog ->
//                        finish()
//                        dialog.dismiss()
//                    }.show(supportFragmentManager, OneButtonDialog.TAG)
                }
            }
        }
        typingGameViewModel.readyCountDown.observe(this) { count ->
            if (count > 0) {
                binding.typingGameReadyCountTv.visibility = View.VISIBLE
                binding.typingGameReadyCountTv.text = count.toString()
            } else {
                binding.typingGameReadyCountTv.visibility = View.GONE
                binding.typingGameTypingEditText.isEnabled = true
                binding.typingGameTypingEditText.text?.clear()
            }
        }
        typingGameViewModel.typingGame.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.typingGameProgressBar.visibility = View.GONE
                    it.data?.let { typingGame ->
                    }
                }

                Status.ERROR -> {
                    binding.typingGameProgressBar.visibility = View.GONE
                }

                Status.LOADING -> {
                    binding.typingGameProgressBar.visibility = View.VISIBLE
                }
            }
        }

        typingGameViewModel.typingGameTimer.observe(this) {
            binding.typingGameTimerTv.text = String.format("%.3f", it)
        }
    }
}