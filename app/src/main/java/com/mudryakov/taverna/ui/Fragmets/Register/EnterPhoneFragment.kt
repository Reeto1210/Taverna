package com.mudryakov.taverna.ui.Fragmets.Register

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.mudryakov.taverna.R

import com.mudryakov.taverna.ui.Fragmets.BaseFragment
import com.mudryakov.taverna.appDatabaseHelper.APP_ACTIVITY
import com.mudryakov.taverna.Objects.changeFragment
import com.mudryakov.taverna.Objects.showToast
import kotlinx.android.synthetic.main.fragment_enter_phone.*
import java.util.concurrent.TimeUnit


class EnterPhoneFragment : BaseFragment(R.layout.fragment_enter_phone) {
    private lateinit var phoneNumber: String
    private lateinit var callBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onResume() {
                         super.onResume()
        APP_ACTIVITY.title = "Введите номер телефона"
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        callBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            }
            override fun onVerificationFailed(p0: FirebaseException) {
                showToast(p0?.message.toString())
            }
            override fun onCodeSent(id: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, p1)
               changeFragment(EnterCodeFragment(phoneNumber, id))
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
                APP_ACTIVITY,
                callBack
            )

        }
    }

    override fun onPause() {
        super.onPause()

    }
}