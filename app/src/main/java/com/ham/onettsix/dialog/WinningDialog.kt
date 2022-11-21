package com.ham.onettsix.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView.OnDismissListener
import androidx.appcompat.app.AppCompatDialogFragment
import com.ham.onettsix.R
import com.ham.onettsix.constant.ResultCode.LOTTERY_FINISHED_LOSE
import com.ham.onettsix.constant.ResultCode.LOTTERY_FINISHED_WIN
import com.ham.onettsix.data.local.PreferencesHelper
import com.ham.onettsix.data.model.Result
import com.ham.onettsix.utils.ProfileImageUtil
import kotlinx.android.synthetic.main.dialog_winning.*
import kotlinx.coroutines.*


class WinningDialog(private val data: Result, private val onDismissListener: DialogDismissListener) : AppCompatDialogFragment() {

    private var isStopGame: Boolean = false

    private var coroutineScope: CoroutineScope? = null

    private var selectedImage1: Int = 0
    private var selectedImage2: Int = 0
    private var selectedImage3: Int = 0

    companion object {
        val TAG: String = WinningDialog::class.java.simpleName
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.setOnKeyListener(DialogInterface.OnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_winning, container, true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startGame()
        dialog_winning_confirm_btn.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun stopGame() {
        coroutineScope?.cancel()
        layout_winning_count_effect.visibility = View.GONE
        layout_winning_result_description.visibility = View.VISIBLE
        when (data.resultCode) {
            LOTTERY_FINISHED_WIN -> {
                Log.d("jhlee", "당첨")
                // 당첨
                selectedImage1 = ProfileImageUtil.getImageId(-1)
                selectedImage2 = ProfileImageUtil.getImageId(-1)
                selectedImage3 = ProfileImageUtil.getImageId(-1)
                winning_slot_img1.setImageResource(R.mipmap.ic_sixsix_man)
                winning_slot_img2.setImageResource(R.mipmap.ic_sixsix_man)
                winning_slot_img3.setImageResource(R.mipmap.ic_sixsix_man)
                winning_result_description.text = getString(R.string.winning_result_win)
                winning_result_sub_description.text = getString(R.string.winning_result_win_sub)
            }
            LOTTERY_FINISHED_LOSE -> {
                // 미당첨
                Log.d("jhlee", "미당첨")
                PreferencesHelper.getInstance(this@WinningDialog.requireContext()).name
                winning_result_description.text = getString(R.string.winning_result_lose, "")
                winning_result_sub_description.text = getString(R.string.winning_result_lose_sub)
                if (selectedImage1 == selectedImage2 && selectedImage2 == selectedImage3) {
                    when ((0..2).random()) {
                        0 -> {
                            winning_slot_img1.setImageResource(ProfileImageUtil.getImageId((selectedImage1 + 1) % 30))
                        }
                        1 -> {
                            winning_slot_img2.setImageResource(
                                ProfileImageUtil.getImageId(
                                    (selectedImage2 + 1) % 30
                                )
                            )
                        }
                        2 -> {
                            winning_slot_img3.setImageResource(
                                ProfileImageUtil.getImageId(
                                    (selectedImage3 + 1) % 30
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope?.cancel()
        onDismissListener.onDismissListener()
    }

    private fun startGame() {
        val delayTime = 50
        CoroutineScope(Dispatchers.Default).launch {
            coroutineScope = this
            isStopGame = false
            var count = 0

            while (true) {
                if (coroutineScope?.isActive == false) {
                    return@launch
                }
                count++
                dialog_winning_count_tv.text = (3 - ((count * delayTime) / 1000)).toString()
                if (count > 60) {
                    CoroutineScope(Dispatchers.Main).launch {
                        stopGame()
                    }
                    return@launch
                } else {
                    delay(delayTime.toLong())
                    val random1 = (0..ProfileImageUtil.IMAGE_COUNT).random()
                    val random2 = (0..ProfileImageUtil.IMAGE_COUNT).random()
                    val random3 = (0..ProfileImageUtil.IMAGE_COUNT).random()
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
                }
            }
        }
    }
}