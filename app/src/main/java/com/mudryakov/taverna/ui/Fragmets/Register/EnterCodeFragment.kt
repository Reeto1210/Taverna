package com.mudryakov.taverna.ui.Fragmets.Register
import com.google.firebase.auth.PhoneAuthProvider
import com.mudryakov.taverna.R
import com.mudryakov.taverna.appDatabaseHelper.*
import com.mudryakov.taverna.ui.Fragmets.BaseFragment
import com.mudryakov.taverna.ui.Fragmets.MainChatList.MainFragment
import com.mudryakov.taverna.Objects.*
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(val phoneNumber: String, val id: String) : BaseFragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = phoneNumber
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)
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
            RestartActivity()
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



              REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid).addOnSuccessListener {
                  REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                      .addOnCompleteListener {
                          if (it.isSuccessful) {
                              showToast("Добро пожаловать")
                              changeFragment(MainFragment())
                          } else {
                              showToast(it.exception?.message.toString())
                          }
                      }
              }
                  .addOnFailureListener { showToast("Произошла ошибка") }
              }

}