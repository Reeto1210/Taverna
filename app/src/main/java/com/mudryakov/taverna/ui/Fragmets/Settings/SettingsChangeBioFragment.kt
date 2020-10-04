package com.mudryakov.taverna.ui.Fragmets.Settings

import com.mudryakov.taverna.R
import com.mudryakov.taverna.appDatabaseHelper.*
import com.mudryakov.taverna.Objects.*
import kotlinx.android.synthetic.main.fragment_settings_change_bio.*


class SettingsChangeBioFragment : settingsBaseFragment(R.layout.fragment_settings_change_bio) {
    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        editTextBio.setText(USER.bio)
    }

    override fun myFunc() {

        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_BIO)
            .setValue(editTextBio.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful)
                {showToast("Данные изменены" )
                fragmentManager?.popBackStack()
                USER.bio = editTextBio.text.toString()}

                else showToast(it.exception?.message.toString())


            }
    }
}