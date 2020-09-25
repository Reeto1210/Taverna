package com.mudryakov.taverna.ui.Fragmets.Register

import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import com.mudryakov.taverna.R
import com.mudryakov.taverna.activityes.MainActivity
import com.mudryakov.taverna.activityes.RegisterActivity
import com.mudryakov.taverna.ui.Fragmets.BaseFragment
import com.mudryakov.taverna.ui.Objects.*
import kotlinx.android.synthetic.main.fragment_enter_code.*


class EnterCodeFragment(val phoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        (activity as RegisterActivity).title = phoneNumber
        super.onStart()

        Register_enterCode.addTextChangedListener(MyTextWhatcher {
            val string = Register_enterCode.text.toString()
            if (string.length >= 6)
                enterCode()
        })
    }

    private fun enterCode() {
        val code = Register_enterCode.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener {

            if (it.isSuccessful) {
                addUser()
            } else {
                showToast(it.exception?.message.toString())
            }
        }

    }
    fun addUser() {
        val uid = AUTH.currentUser?.uid.toString()
        var dateMap = mutableMapOf<String, Any>()

        dateMap[CHILD_ID] = uid
        dateMap[CHILD_PHONE] = phoneNumber

               REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Добро пожаловать")
                    (activity as RegisterActivity).replaceActivity(MainActivity())
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }
}