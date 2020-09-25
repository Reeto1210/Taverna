import com.mudryakov.taverna.R
import com.mudryakov.taverna.activityes.MainActivity
import com.mudryakov.taverna.ui.Fragmets.Settings.settingsBaseFragment
import com.mudryakov.taverna.ui.Objects.*
import kotlinx.android.synthetic.main.fragment_settings_change_full_name.*

class SettingsChangeUserFullName : settingsBaseFragment(R.layout.fragment_settings_change_full_name) {
    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        setFullname()
    }


    private fun setFullname() {
        val fullName = USER.fullName.split(" ")
        editTextPersonName.setText(fullName[0])
        if (fullName.size > 1) editTextSecondName.setText(fullName[1])
    }




    override fun myFunc() {
        val fullName: String
        val name = editTextPersonName.text
        val secondName = editTextSecondName.text
        fullName = when {
                name.contains(" ") || secondName.contains(" ") -> {
                showToast("данные не могу содержать пробелы")
                return
            }
            name.isNotEmpty() && secondName.isNotEmpty() -> "${editTextPersonName.text} ${editTextSecondName.text}"
            name.isNotEmpty() -> name.toString()
            else -> {
                showToast("Имя не может быть пустым")
                return
            }

        }
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FULL_NAME)
            .setValue(fullName.toLowerCase())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.Toast_change_data))
                    USER.fullName = fullName
                    (activity as MainActivity).myDrawer.updateProfile()
                    fragmentManager?.popBackStack()

                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }



}