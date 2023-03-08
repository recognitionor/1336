package com.ham.onettsix.viewmodel

import androidx.lifecycle.ViewModel
import com.ham.onettsix.data.api.ApiHelper
import com.ham.onettsix.data.local.DatabaseHelper
import com.ham.onettsix.data.local.PreferencesHelper

class QuizGameViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper,
    private val preferenceHelper: PreferencesHelper?
) : ViewModel() {

    companion object {
        const val PARAM_KEY_START_PAGE = "startPage"

        const val PARAM_KEY_CONTENT_COUNT = "contentCount"

        const val PARAM_KEY_INVESTMENT_TAG_ID = "investmentTagId"
    }


}
