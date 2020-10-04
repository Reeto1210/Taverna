package com.mudryakov.taverna.ui.Fragmets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mudryakov.taverna.MainActivity

open class BaseFragment(private val layout: Int) : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layout, container, false)
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).myDrawer.disableDrawer()
    }



}