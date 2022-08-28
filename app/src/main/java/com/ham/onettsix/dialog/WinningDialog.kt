package com.ham.onettsix.dialog

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.R
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.utils.ProfileImageUtil
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.WinningGameViewModel
import kotlinx.android.synthetic.main.dialog_winning.*
import kotlinx.coroutines.*
import kotlin.random.Random


class WinningDialog : AppCompatDialogFragment() {

    private var isStopGame: Boolean = false

    private var coroutineScope: CoroutineScope? = null

    private var selectedImage: Int = 0

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
        val mainView = inflater.inflate(R.layout.dialog_winning, container, true)
//        this.isCancelable = false
//        this.dialog?.setCanceledOnTouchOutside(false)
        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startGame()
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
                val random1 = (0..29).random()
                val random2 = (0..29).random()
                val random3 = (0..29).random()
                winning_slot_img1.post {
                    winning_slot_img1.setImageResource(ProfileImageUtil.getImageId(random1))
                }

                winning_slot_img2.post {
                    winning_slot_img2.setImageResource(ProfileImageUtil.getImageId(random2))
                }

                winning_slot_img3.post {
                    winning_slot_img3.setImageResource(ProfileImageUtil.getImageId(random3))
                }

                count++
                delay(50)
            }
        }
    }
}