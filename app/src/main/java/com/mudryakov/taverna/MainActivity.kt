package com.mudryakov.taverna

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.mudryakov.taverna.appDatabaseHelper.*
import com.mudryakov.taverna.databinding.ActivityMainBinding
import com.mudryakov.taverna.ui.Fragmets.MainChatList.MainFragment
import com.mudryakov.taverna.ui.Fragmets.Register.EnterPhoneFragment
import com.mudryakov.taverna.Objects.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    lateinit var myDrawer: AppDrawer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APP_ACTIVITY = this

        initFireBase()

            initUser {
            CoroutineScope(Dispatchers.IO).launch{
                initContacts()
            }

            initFields()
            initFunc()
        }
         mBinding = ActivityMainBinding.inflate(layoutInflater)
         setContentView(mBinding.root)


    }


    private fun initFields() {
        TOOLBAR = mBinding.toolbarMain
        myDrawer = AppDrawer()

    }


    fun initFunc() {

            setSupportActionBar(TOOLBAR)
            changeFragment(MainFragment(), false)
           if (AUTH.currentUser == null)
            changeFragment(EnterPhoneFragment(),false)
        myDrawer.Create()
    }



    override fun onPause() {
        super.onPause()
        appStatus.changeState(appStatus.OFFLINE)
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
        initContacts()
    }
    }

    override fun onResume() {
        super.onResume()
        appStatus.changeState(appStatus.ONLINE)

    }


}