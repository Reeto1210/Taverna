package com.mudryakov.taverna.Objects

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mudryakov.taverna.appDatabaseHelper.APP_ACTIVITY

const val READ_CONTACTS = Manifest.permission.READ_CONTACTS
const val RECOR_AUDIO = Manifest.permission.RECORD_AUDIO
const val WRITE_FILES = Manifest.permission.WRITE_EXTERNAL_STORAGE


const val REQUEST_CODE = 200

fun checkPermission(permission: String): Boolean {
    return if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(APP_ACTIVITY, permission) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission), REQUEST_CODE)
        false
    } else true
}

