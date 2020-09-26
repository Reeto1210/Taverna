package com.mudryakov.taverna.ui.Fragmets.Settings

import ChangeUserNameFragment
import SettingsChangeUserFullName
import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.StorageReference
import com.mudryakov.taverna.R
import com.mudryakov.taverna.activityes.MainActivity
import com.mudryakov.taverna.activityes.RegisterActivity
import com.mudryakov.taverna.ui.Fragmets.BaseFragment
import com.mudryakov.taverna.ui.Objects.*
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_settings.*
import java.lang.Exception


class SettingsFragment : BaseFragment(R.layout.fragment_settings) {



    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)

                addInfoUser()
        btnSettingsChangeUsername.setOnClickListener {
            APP_ACTIVITY.changeFragment(
                ChangeUserNameFragment()
            )
        }
        btnSettingsChangeAboutMe.setOnClickListener {
            APP_ACTIVITY.changeFragment(
                SettingsChangeBioFragment()
            )
        }
        SettingsBtnNewAvatar.setOnClickListener {
            changeProfileImg()
        }


    }

    private fun changeProfileImg() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_menu, menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit -> {
                AUTH.signOut()
                (APP_ACTIVITY).replaceActivity(RegisterActivity())
                APP_ACTIVITY.finish()
            }
            R.id.changeName ->
                APP_ACTIVITY.changeFragment(SettingsChangeUserFullName())
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null
            && resultCode == RESULT_OK
        ) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(NODE_PROFILE_IMG).child(CURRENT_UID)
                putImageToStorage(path, uri) {
                downloadUrl(path) {
                    addUrlBase(it) {
                        USER.photoUrl = it
                        ic_settings_profile.downloadAndSetImage(it)
                        showToast("Данные обновлены")
                        (activity as MainActivity).myDrawer.updateProfile()
                    }
                }
            }
        }


    }


    fun addInfoUser() {

        tvUserNameSettings.text = setFullnameUi()
        SettingsPhoneNumber.text = USER.phoneNumber
        SettingsAboutMe.text = USER.bio
        tvSettingsOnline.text = USER.status
        SettingsUsername.text = USER.username
        try {
            ic_settings_profile.downloadAndSetImage(USER.photoUrl)
        } catch (e: Exception) {
        }
    }


}
