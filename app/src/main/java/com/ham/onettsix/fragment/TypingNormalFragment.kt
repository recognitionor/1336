package com.ham.onettsix.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ham.onettsix.R
import com.ham.onettsix.adapter.TypingGameNormalAdapter
import com.ham.onettsix.databinding.FragmentTypingNormalBinding


class TypingNormalFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(): TypingNormalFragment {
            return TypingNormalFragment()
        }
    }

    private lateinit var binding: FragmentTypingNormalBinding

    private lateinit var typeTypingGameNormalAdapter: TypingGameNormalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTypingNormalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.typeTypingGameNormalAdapter = TypingGameNormalAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.typingGameNormalRv.layoutManager = LinearLayoutManager(context)
        binding.typingGameNormalRv.adapter = this.typeTypingGameNormalAdapter

    }
}