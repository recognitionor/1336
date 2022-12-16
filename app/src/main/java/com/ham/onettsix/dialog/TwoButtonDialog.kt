package com.ham.onettsix.dialog

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.ham.onettsix.R
import com.ham.onettsix.databinding.DialogTwoButtonBinding
import kotlinx.coroutines.*


class TwoButtonDialog(
    private var title: String,
    private var content: String,
    private var callback: (isPositive: Boolean, dialog: DialogFragment) -> Unit
) : AppCompatDialogFragment() {

    private lateinit var binding: DialogTwoButtonBinding

    companion object {
        val TAG: String = TwoButtonDialog::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogTwoButtonBinding.inflate(layoutInflater)
        binding.twoButtonTitle.text = title
        binding.twoButtonContent.text = content
        binding.twoButtonPositive.setOnClickListener {
            callback.invoke(true, this@TwoButtonDialog)
        }
        binding.twoButtonNegative.setOnClickListener {
            callback.invoke(false, this@TwoButtonDialog)
        }
        return binding.root
    }
}