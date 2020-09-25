import com.mudryakov.taverna.R
import com.mudryakov.taverna.ui.Fragmets.Settings.settingsBaseFragment
import com.mudryakov.taverna.ui.Objects.*
import kotlinx.android.synthetic.main.fragment_change_username.*
import java.util.*

class ChangeUserNameFragment : settingsBaseFragment(R.layout.fragment_change_username) {
    override fun onResume() {
        super.onResume()
        editTextUserName.setText(USER.username)
        editTextUserName.addTextChangedListener(MyTextWhatcher {
            if (USER.username != editTextUserName.text.toString().toLowerCase(Locale.ROOT))
            setHasOptionsMenu(true)
            else
                setHasOptionsMenu(false)
        })
    }

    private fun tryChangeUserName() {
        if (editTextUserName.text.isNotEmpty()) {
            changeUserName()
        } else showToast("ник не может быть пустым")

    }

    private fun changeUserName() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USERNAME)
            .setValue(editTextUserName.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Данные изменены")
                    REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()
                    USER.username = editTextUserName.text.toString()


                    REF_DATABASE_ROOT.child(NODE_USERNAMES)
                        .child(USER.username.toLowerCase(Locale.ROOT)).setValue(CURRENT_UID)

                    fragmentManager?.popBackStack()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    override fun myFunc() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES)
            .addListenerForSingleValueEvent(appValueEventListener {
                if (it.hasChild(
                        editTextUserName.text.toString().toLowerCase(Locale.ROOT)
                    )
                )
                    showToast("Этот ник занят")
                else tryChangeUserName()
            })

    }




}