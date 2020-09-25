package com.mudryakov.taverna.activityes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.mudryakov.taverna.R
import com.mudryakov.taverna.databinding.ActivityRegisterBinding
import com.mudryakov.taverna.ui.Fragmets.Register.EnterPhoneFragment
import com.mudryakov.taverna.ui.Objects.initFireBase

class RegisterActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityRegisterBinding
    lateinit var mToolbar: Toolbar
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
       initFireBase()
        mToolbar = mBinding.RegisterToolbar
        setSupportActionBar(mToolbar)
        title = getString(R.string.Register_give_ur_number)
        supportFragmentManager.beginTransaction()
            .add(R.id.RegisterContainer, EnterPhoneFragment())
            .commit()
    }
}