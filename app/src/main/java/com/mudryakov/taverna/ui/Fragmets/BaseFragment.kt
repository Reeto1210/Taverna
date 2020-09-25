package com.mudryakov.taverna.ui.Fragmets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mudryakov.taverna.R
import com.mudryakov.taverna.activityes.MainActivity
import com.mudryakov.taverna.databinding.FragmentChatBinding
import com.mudryakov.taverna.ui.Objects.APP_ACTIVITY
import com.mudryakov.taverna.ui.Objects.hideKeyBoard

open class BaseFragment(private val layout: Int) : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layout, container, false)
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).myDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).myDrawer.enableDrawer()
        hideKeyBoard()
    }

}