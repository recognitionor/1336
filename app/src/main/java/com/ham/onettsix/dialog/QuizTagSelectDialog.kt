package com.ham.onettsix.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ham.onettsix.adapter.QuizTagAdapter
import com.ham.onettsix.databinding.DialogQuizTagSelectBinding
import com.ham.onettsix.fragment.InvestmentFragment


class QuizTagSelectDialog(private val onDismissListener: DialogDismissListener) :
    AppCompatDialogFragment(), View.OnClickListener {

    companion object {
        val TAG: String = QuizTagSelectDialog::class.java.simpleName
    }

    lateinit var binding: DialogQuizTagSelectBinding

    val quizTagAdapter by lazy {
        QuizTagAdapter()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        binding = DialogQuizTagSelectBinding.inflate(layoutInflater)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DialogQuizTagSelectBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.quizTagRv.adapter = quizTagAdapter
        binding.quizTagRv.setOnClickListener(this)
        binding.quizTagRv.layoutManager = GridLayoutManager(
            context, InvestmentFragment.GRID_COUNT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onDestroy() {
        super.onDestroy()
        onDismissListener.onDismissListener()
    }

    override fun onClick(p0: View?) {
    }
}