import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.mudryakov.taverna.R
import com.mudryakov.taverna.models.CommonModel
import com.mudryakov.taverna.ui.Fragmets.BaseFragment
import com.mudryakov.taverna.ui.Objects.APP_ACTIVITY

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {
   lateinit var mRecycleView:RecyclerView
    lateinit var adapter:FirebaseRecyclerAdapter<CommonModel, contactHolder>

    class contactHolder (view: View):RecyclerView.ViewHolder(view){

    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.Contacts_title)
    }
}