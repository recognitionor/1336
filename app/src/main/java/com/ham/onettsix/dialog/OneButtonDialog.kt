package com.ham.onettsix.dialog

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.ham.onettsix.R
import com.ham.onettsix.databinding.ActivityProfileDetailBinding
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
    ): View? {
        binding = DialogOneButtonBinding.inflate(layoutInflater)
        val view = inflater.inflate(R.layout.dialog_one_button, container, true)
        binding.oneButtonTitle.text = title
        binding.oneButtonContent.text = content
        binding.oneButtonBtn.setOnClickListener { callback.invoke(this@OneButtonDialog) }
        return view
    }
}