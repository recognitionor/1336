package com.ham.onettsix.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.MainActivity
import com.ham.onettsix.R
import com.ham.onettsix.constant.ResultCode
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.databinding.FragmentRpsGameBinding
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.RPSGameViewModel
import kotlinx.coroutines.*

class RPSGameFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentRpsGameBinding

    private val rpsGameViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[RPSGameViewModel::class.java]
    }

    private var isStopGame = true
    private var serverResult: Int? = null
    private var animationDelay: Long = 50
    private var coroutineScope: CoroutineScope? = null
    private var selectedItem: Int = 0
    private var selectedImage: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserve()
        rpsGameViewModel.updateUserInfo()

        binding.gameImageRock.setOnClickListener(this@RPSGameFragment)
        binding.gameImageScissors.setOnClickListener(this@RPSGameFragment)
        binding.gameImagePaper.setOnClickListener(this@RPSGameFragment)
        binding.gameStartBtn.setOnClickListener(this@RPSGameFragment)
        binding.gameInfoMessageImg.setOnClickListener(this@RPSGameFragment)
        binding.layoutGameNeededLogin.rpsGameLoginBtn.setOnClickListener(this@RPSGameFragment)

    }

    private fun setupObserve() {
        rpsGameViewModel.userInfo.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.layoutGameNeededLogin.layoutGameNeededLogin.visibility = View.GONE
                }
                else -> {
                    binding.layoutGameNeededLogin.layoutGameNeededLogin.visibility = View.VISIBLE
                }
            }
        }

        rpsGameViewModel.gameResult.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.SUCCESS -> {
                    it.data?.resultCode?.let { resultCode ->
                        onGameStop(resultCode)
                    }
                }
                Status.ERROR -> {
                    it.data?.resultCode?.let { resultCode ->
                        onGameStop(resultCode)
                    }
                }
                else -> {}
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRpsGameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope?.cancel()
        viewModelStore.clear()
        rpsGameViewModel.gameResult.removeObservers(viewLifecycleOwner)
    }

    private fun requestGame() {
        animationDelay = 200
        if (serverResult == null) {
            rpsGameViewModel.getRockPaperScissors()
        } else {
            serverResult?.let {
                onGameStop(it)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {

            binding.layoutGameNeededLogin.rpsGameLoginBtn -> {
                (parentFragment as GameFragment).login()
            }

            binding.gameInfoMessageImg -> {
                rpsGameViewModel.getRockPaperScissors()
            }

            binding.gameImageRock -> {
                if (!isStopGame) {
                    binding.gameImageScissors.visibility = View.INVISIBLE
                    binding.gameImagePaper.visibility = View.INVISIBLE
                    selectedItem = 0
                    requestGame()
                }
                return
            }
            binding.gameImageScissors -> {
                if (!isStopGame) {
                    binding.gameImageRock.visibility = View.INVISIBLE
                    binding.gameImagePaper.visibility = View.INVISIBLE
                    selectedItem = 1
                    requestGame()
                }
                return
            }
            binding.gameImagePaper -> {
                if (!isStopGame) {
                    binding.gameImageRock.visibility = View.INVISIBLE
                    binding.gameImageScissors.visibility = View.INVISIBLE
                    selectedItem = 2
                    requestGame()
                }
                return
            }
            binding.gameStartBtn -> {
                if (isStopGame) {
                    binding.gameImageRock.visibility = View.VISIBLE
                    binding.gameImageScissors.visibility = View.VISIBLE
                    binding.gameImagePaper.visibility = View.VISIBLE
                    binding.gameStartBtn.visibility = View.GONE
                    animationDelay = 50
                    gameStart()
                }
            }
        }
    }


    private fun gameStart() {
        binding.gameResultTv.visibility = View.GONE
        binding.gameResultTv.text = ""
        CoroutineScope(Dispatchers.Default).launch {
            coroutineScope = this
            isStopGame = false
            var count = 0

            while (true) {
                selectedImage = when (count % 3) {
                    0 -> {
                        R.mipmap.ic_rock
                    }
                    1 -> {
                        R.mipmap.ic_scissors
                    }
                    else -> {
                        R.mipmap.ic_paper
                    }
                }
                count++
                delay(animationDelay)

                binding.gameImageView.post {
                    binding.gameImageView.setImageResource(selectedImage)
                }
            }
        }
    }

    fun loginUpdate() {
        binding.layoutGameNeededLogin.layoutGameNeededLogin.visibility = View.GONE
        (requireParentFragment() as GameFragment).updateMyTicket()
        (this@RPSGameFragment.activity as MainActivity).mainViewModel.updateUserInfo()
    }

    private fun onGameStop(result: Int, isError: Boolean = false) {
        (parentFragment as GameFragment).apply {
            this.updateMyTicket(true)
        }

        coroutineScope?.cancel()
        isStopGame = true
        binding.gameResultTv.visibility = View.VISIBLE
        binding.gameStartBtn.visibility = View.VISIBLE
        binding.gameStartBtn.setText(R.string.game_rock_scissors_paper_restart)
        when (result) {
            ResultCode.NO_TICKET -> {
                binding.gameStartBtn.visibility = View.VISIBLE
                binding.gameInfoMessageImg.visibility = View.VISIBLE
                binding.gameInfoMessageImg.visibility = View.VISIBLE
                binding.gameInfoMessageTv.setText(R.string.no_ticket)
                binding.gameLoadProgress.visibility = View.GONE
            }

            ResultCode.RPC_WIN -> {
                binding.gameResultTv.visibility = View.VISIBLE
                binding.gameResultTv.setText(R.string.game_win)
                binding.gameStartBtn.setText(R.string.game_rock_scissors_paper_restart)
                selectedImage = when (selectedItem) {
                    0 -> {
                        R.mipmap.ic_scissors
                    }
                    1 -> {
                        R.mipmap.ic_paper
                    }
                    else -> {
                        R.mipmap.ic_rock
                    }
                }
            }
            ResultCode.RPC_LOSE -> {
                val random = (0..1).random()
                if (random == 0) {
                    // 무승부 케이스
                    binding.gameResultTv.setText(R.string.game_draw)
                    selectedImage = when (selectedItem) {
                        0 -> {
                            R.mipmap.ic_rock
                        }
                        1 -> {
                            R.mipmap.ic_scissors
                        }
                        else -> {
                            R.mipmap.ic_paper
                        }
                    }
                } else {
                    // 패배의 경우
                    binding.gameResultTv.setText(R.string.game_lose)
                    selectedImage = when (selectedItem) {
                        0 -> {
                            R.mipmap.ic_paper
                        }
                        1 -> {
                            R.mipmap.ic_rock
                        }
                        else -> {
                            R.mipmap.ic_scissors
                        }
                    }
                }
            }
        }
        binding.gameImageView.setImageResource(selectedImage)
    }

    fun updateCountText(gameCount: Int, maxCount: Int) {
        binding.gameCountTv.text =
            "$gameCount${getString(R.string.count_divide_mark, "d")}$maxCount"
    }

    fun enableTicket(isEnable: Boolean) {
        binding.gameStartBtn.isEnabled = isEnable
    }
}
