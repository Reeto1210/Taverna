package com.mudryakov.taverna.Objects

import android.text.Editable
import android.text.TextWatcher

class MyTextWhatcher(val onSucces: (Editable?) -> Unit):TextWatcher{

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
           }

    override fun afterTextChanged(p0: Editable?) {
    onSucces(p0)
    }
}