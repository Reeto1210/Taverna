package com.mudryakov.taverna.ui.Fragmets.Register

import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.mudryakov.taverna.R
import com.mudryakov.taverna.activityes.RegisterActivity
import com.mudryakov.taverna.ui.Fragmets.BaseFragment
import com.mudryakov.taverna.ui.Objects.changeFragment
import com.mudryakov.taverna.ui.Objects.showToast
import kotlinx.android.synthetic.main.fragment_enter_phone.*
import java.util.concurrent.TimeUnit


class EnterPhoneFragment : Fragment(R.layout.fragment_enter_phone) {
    private lateinit var phoneNumber: String
    private lateinit var callBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onStart() {
        super.onStart()
        callBack = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0?.message.toString())
            }

            override fun onCodeSent(id: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, p1)
                changeFragment(EnterCodeFragment(phoneNumber,id))
            }
        }
        register_btn_next.setOnClickListener { sentCode() }
    }

    private fun sentCode() {
        if (RegisterEnterPhone.text.toString().isEmpty())
            showToast("Введите номер телефона")
        else {
            phoneNumber = RegisterEnterPhone.text.toString()
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                30,
                TimeUnit.SECONDS,
                activity as RegisterActivity,
                callBack
            )

                  }
    }
}