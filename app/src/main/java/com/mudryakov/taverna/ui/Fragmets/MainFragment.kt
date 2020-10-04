package com.mudryakov.taverna.ui.Fragmets

import androidx.fragment.app.Fragment
import com.mudryakov.taverna.R
import com.mudryakov.taverna.MainActivity
import com.mudryakov.taverna.appDatabaseHelper.APP_ACTIVITY
import com.mudryakov.taverna.Objects.hideKeyBoard


class MainFragment : Fragment(R.layout.fragment_chat) {


    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Taverna"
        (activity as MainActivity).myDrawer.enableDrawer()
        hideKeyBoard()
    }
}