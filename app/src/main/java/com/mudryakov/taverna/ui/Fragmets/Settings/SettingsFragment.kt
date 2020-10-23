package com.mudryakov.taverna.ui.Fragmets.Settings

import ChangeUserNameFragment
import SettingsChangeUserFullName
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.*
import com.mudryakov.taverna.R
import com.mudryakov.taverna.appDatabaseHelper.*

import com.mudryakov.taverna.ui.Fragmets.BaseFragment
import com.mudryakov.taverna.Objects.*
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
           changeFragment(
                ChangeUserNameFragment()
            )
        }
        btnSettingsChangeAboutMe.setOnClickListener {
            changeFragment(
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
            .setRequestedSize(250, 250)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_menu, menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit -> {
               greateDialogForConfirm("Вы действительно хотите выйти?"){
                   appStatus.changeState(appStatus.OFFLINE)
                   AUTH.signOut()
                   RestartActivity()

                   }


            }
            R.id.changeName ->
                changeFragment(SettingsChangeUserFullName())
        }
        return true
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null
            && resultCode == RESULT_OK
        ) {
            val uri = CropImage.getActivityResult(data).uri
              val path = REF_STORAGE_ROOT.child(NODE_PROFILE_IMG)

            putFileToStorage(path, uri) { //uri - kartinka resultat activnosti cropa
                downloadUrl(path) {
                    addUrlBase(it) { //it -> URL
                      REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FILE_URL).setValue(it)
                        USER.photoUrl = it
                        ic_settings_profile.downloadAndSetImage(USER.photoUrl)

                    }
                }
            }
        }


    }

}
