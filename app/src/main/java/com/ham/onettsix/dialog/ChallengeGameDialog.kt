package com.ham.onettsix.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.ham.onettsix.R
import com.ham.onettsix.databinding.DialogChallengeGameBinding


class ChallengeGameDialog(
    private var remainTicket: Int,
    private var remainChance: Long,
    private var callback: (isPositive: Boolean, remainTicket: Int, dialog: DialogFragment) -> Unit
) : AppCompatDialogFragment() {

    private lateinit var binding: DialogChallengeGameBinding

    companion object {
        val TAG: String = ChallengeGameDialog::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DialogChallengeGameBinding.inflate(layoutInflater)
        binding.twoButtonPositive.isEnabled = false
        if (remainTicket > remainChance) {
            binding.dialogChallengeSeekbar.max = remainChance.toInt()
        } else {
            binding.dialogChallengeSeekbar.max = remainTicket
        }
        binding.dialogChallengeSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekbar: SeekBar?, value: Int, p2: Boolean) {
                binding.dialogChallengeSeekbarTv.text = "$value ${getString(R.string.count)}"
                binding.dialogChallengeChanceTv.text = getString(
                    R.string.game_try_dialog_chance,
                    String.format("%.3f", ((value.toDouble() / remainChance) * 100))
                ) + "%"
                binding.twoButtonPositive.isEnabled = value > 0
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
        binding.twoButtonTitle.text = getString(R.string.game_try_dialog_title)
        binding.twoButtonContent.text = getString(R.string.game_try_dialog_content)
        binding.twoButtonPositive.setOnClickListener {
            callback.invoke(true, binding.dialogChallengeSeekbar.progress, this@ChallengeGameDialog)
        }
        binding.twoButtonNegative.setOnClickListener {
            callback.invoke(
                false, binding.dialogChallengeSeekbar.progress, this@ChallengeGameDialog
            )
        }
        return binding.root
    }
}