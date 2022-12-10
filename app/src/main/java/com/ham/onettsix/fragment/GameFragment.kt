package com.ham.onettsix.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.ham.onettsix.LoginActivity
import com.ham.onettsix.R
import com.ham.onettsix.constant.ActivityResultKey
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment : Fragment(R.layout.fragment_game) {

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == ActivityResultKey.LOGIN_RESULT_OK) {
                this@GameFragment.rps_game_fragment.getFragment<RPSGameFragment>().loginUpdate()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, null)
    }

    fun login() {
        activityResult.launch(Intent(requireActivity(), LoginActivity::class.java))
    }
}
