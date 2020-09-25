package com.mudryakov.taverna.ui.Fragmets.Settings

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.mudryakov.taverna.R
import com.mudryakov.taverna.ui.Fragmets.BaseFragment

open class settingsBaseFragment(private val layout: Int) :BaseFragment(layout) {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.register_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.registerMenuOkey -> myFunc()
        }
return true
    }
    open fun myFunc() {
    }

}
