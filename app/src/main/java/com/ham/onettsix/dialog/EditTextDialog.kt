package com.ham.onettsix.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.ham.onettsix.R
import com.ham.onettsix.databinding.ActivityProfileDetailBinding
import com.ham.onettsix.databinding.DialogEditTextBinding
import com.ham.onettsix.databinding.DialogOneButtonBinding

class EditTextDialog(
    private var title: String = "",
    private var callback: (dialog: DialogFragment, str: String) -> Unit
) : AppCompatDialogFragment() {

    private lateinit var binding: DialogEditTextBinding

    companion object {
        val TAG: String = EditTextDialog::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DialogEditTextBinding.inflate(layoutInflater)
        binding.editDialogTitle.text = title

        binding.editDialogConfirm.isEnabled =
            binding.editDialogEditText.text.toString().isNotEmpty()
        binding.editDialogEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                binding.editDialogConfirm.isEnabled = (s?.length ?: 0) > 0
            }

        })
        binding.editDialogConfirm.setOnClickListener {
            callback.invoke(
                this@EditTextDialog, binding.editDialogEditText.text.toString()
            )
        }
        return binding.root
    }
}