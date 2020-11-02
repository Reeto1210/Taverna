package com.mudryakov.taverna.ui.Fragmets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mudryakov.taverna.MainActivity
import com.mudryakov.taverna.Objects.hideKeyBoard

open class BaseFragment(private val layout: Int) : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layout, container, false)
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).myDrawer.disableDrawer()
    hideKeyBoard()
    }


    override fun onPause() {
        super.onPause()
        hideKeyBoard()
    }

}