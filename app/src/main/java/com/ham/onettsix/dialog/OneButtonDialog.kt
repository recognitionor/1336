package com.ham.onettsix.dialog

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.ham.onettsix.databinding.DialogOneButtonBinding

class OneButtonDialog(
    private var title: String = "",
    private var content: String = "",
    private var callback: (dialog: DialogFragment) -> Unit
) :
    AppCompatDialogFragment() {

    private lateinit var binding: DialogOneButtonBinding

    companion object {
        val TAG: String = OneButtonDialog::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogOneButtonBinding.inflate(layoutInflater)
        binding.oneButtonTitle.text = title
        binding.oneButtonContent.text = content
        binding.oneButtonBtn.setOnClickListener { callback.invoke(this@OneButtonDialog) }
        return binding.root
    }
}