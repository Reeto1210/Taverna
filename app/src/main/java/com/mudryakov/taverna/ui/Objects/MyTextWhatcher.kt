package com.mudryakov.taverna.ui.Objects

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_enter_code.*

class MyTextWhatcher(val onSucces: (Editable?) -> Unit):TextWatcher{

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
           }

    override fun afterTextChanged(p0: Editable?) {
    onSucces(p0)
    }
}