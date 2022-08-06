package com.ham.onettsix.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ham.onettsix.R
import com.ham.onettsix.data.api.ApiHelperImpl
import com.ham.onettsix.data.api.RetrofitBuilder
import com.ham.onettsix.data.local.DatabaseBuilder
import com.ham.onettsix.data.local.DatabaseHelperImpl
import com.ham.onettsix.utils.Status
import com.ham.onettsix.utils.ViewModelFactory
import com.ham.onettsix.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.view_game_layout.*
import kotlinx.android.synthetic.main.view_game_layout.view.*

class GameFragment : Fragment() {

    var slotTv1: Int = 0;
    var slotTv2: Int = 0;
    var slotTv3: Int = 0;

    private val gameViewModel by lazy {
        ViewModelProviders.of(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity().applicationContext))
            )
        )[GameViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObserve()

    }

    private fun setupObserve() {
        Log.d("jhlee", "setupObserve")
        gameViewModel.result.observe(this) {
            Log.d("jhlee", "observe")
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d("jhlee", "SUCCESS")
                    layout_game_start.visibility = View.GONE
                    game_layout.start()
                    return@observe
                }
                Status.LOADING -> {
                    Log.d("jhlee", "loading")
                    game_load_progress.visibility = View.VISIBLE
                    game_start_btn.visibility = View.GONE
                    return@observe
                }
                Status.ERROR -> {
                    Log.d("jhlee", "ERROR")
                    return@observe
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_game, null).apply {
            layout_game_start.setOnClickListener {
                gameViewModel.game()
            }
        }
    }
}