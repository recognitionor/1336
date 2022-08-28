package com.ham.onettsix.dialog

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.R
import com.ham.onettsix.constant.ResultCode
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.utils.ProfileImageUtil
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.WinningGameViewModel
import kotlinx.android.synthetic.main.dialog_winning.*
import kotlinx.coroutines.*
import kotlin.random.Random


class WinningDialog : AppCompatDialogFragment() {

    private var isStopGame: Boolean = false

    private var coroutineScope: CoroutineScope? = null

    private var selectedImage1: Int = 0
    private var selectedImage2: Int = 0
    private var selectedImage3: Int = 0

    private val winningGameViewModel by lazy {
        ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[WinningGameViewModel::class.java]
    }

    companion object {
        val TAG: String = WinningDialog::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_winning, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        winningGameViewModel.validateLimitedRv()
        startGame()
    }

    private fun setupObserver() {
        winningGameViewModel.winningViewModel.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d("jhlee", "result : ${it.data}")
                    coroutineScope?.cancel()
                    winning_result_description.text = it.data?.description ?: ""
                    if (it.data?.resultCode == ResultCode.LOTTERY_FINISHED_WIN) {
                        Log.d("jhlee", "result : ${it.data}")
                        // 당첨
                        coroutineScope?.cancel()
                        selectedImage1 = ProfileImageUtil.getImageId(-1)
                        selectedImage2 = ProfileImageUtil.getImageId(-1)
                        selectedImage3 = ProfileImageUtil.getImageId(-1)
                        winning_slot_img1.setImageResource(R.mipmap.ic_sixsix_man)
                        winning_slot_img2.setImageResource(R.mipmap.ic_sixsix_man)
                        winning_slot_img3.setImageResource(R.mipmap.ic_sixsix_man)
                    } else {
                        // 미당첨
                        coroutineScope?.cancel()
                        if (it.data?.resultCode == ResultCode.LOTTERY_FINISHED) {
                            winning_slot_img1.visibility = View.INVISIBLE
                            winning_slot_img2.setImageResource(R.mipmap.ic_sixsix_disappointed)
                            winning_slot_img3.visibility = View.INVISIBLE
                        } else if (it.data?.resultCode == ResultCode.LOTTERY_FINISHED_LOSE){
                            if (selectedImage1 == selectedImage2 && selectedImage2 == selectedImage3) {
                                when ((0..2).random()) {
                                    0 -> {
                                        winning_slot_img1.setImageResource(ProfileImageUtil.getImageId((selectedImage1 + 1) % 30))
                                    }
                                    1 -> {
                                        winning_slot_img2.setImageResource(ProfileImageUtil.getImageId((selectedImage2 + 1) % 30))
                                    }
                                    2 -> {
                                        winning_slot_img3.setImageResource(ProfileImageUtil.getImageId((selectedImage3 + 1) % 30))
                                    }
                                }
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    winning_result_description.setText(R.string.common_error)
                    coroutineScope?.cancel()
                }
                Status.LOADING -> {

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope?.cancel()
    }

    private fun startGame() {
        CoroutineScope(Dispatchers.Default).launch {
            coroutineScope = this
            isStopGame = false
            var count = 0

            while (true) {
                val random1 = (0..32).random()
                val random2 = (0..32).random()
                val random3 = (0..32).random()
                selectedImage1 = ProfileImageUtil.getImageId(random1)
                selectedImage2 = ProfileImageUtil.getImageId(random2)
                selectedImage3 = ProfileImageUtil.getImageId(random3)

                winning_slot_img1.post {
                    winning_slot_img1.setImageResource(selectedImage1)
                }
                winning_slot_img2.post {
                    winning_slot_img2.setImageResource(selectedImage2)
                }
                winning_slot_img3.post {
                    winning_slot_img3.setImageResource(selectedImage3)
                }

                count++
                delay(50)
            }
        }
    }
}