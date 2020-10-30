package com.mudryakov.taverna.ui.Fragmets.MainChatList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.*
import com.mudryakov.taverna.R
import com.mudryakov.taverna.appDatabaseHelper.TYPE_FILE
import com.mudryakov.taverna.appDatabaseHelper.TYPE_IMAGE
import com.mudryakov.taverna.appDatabaseHelper.TYPE_TEXT
import com.mudryakov.taverna.appDatabaseHelper.TYPE_VOICE
import com.mudryakov.taverna.models.MainListModel
import com.mudryakov.taverna.ui.Fragmets.SingleChat.SingleChatFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.main_list_item.view.*

var mutableListMainListAdapter = mutableListOf<MainListModel>()

class MainLIstRecycleAdapter : RecyclerView.Adapter<MainLIstRecycleAdapter.MainListViewHolder>() {


    class MainListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout1: ConstraintLayout = view.contact_item_layout
        val mainListPhoto: CircleImageView = view.mainListPhoto
        val mainListFullName: TextView = view.contact_fullname
        val mainListLastMessage: TextView = view.mainListLastMessage
        val mainListLastMessageTime: TextView = view.mainListMessageTime

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        val myHolder = MainListViewHolder(view)
        myHolder.layout1.setOnClickListener {
            changeFragment(
                SingleChatFragment(
                    mutableListMainListAdapter[myHolder.adapterPosition].user
                )
            )
        }
        return myHolder

    }

    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        val currentMainListModel = mutableListMainListAdapter[position]
        holder.mainListFullName.text = currentMainListModel.user.fullName
        holder.mainListPhoto.downloadAndSetImage(currentMainListModel.user.photoUrl)
       if (currentMainListModel.message.time != ""){
           holder.mainListLastMessageTime.text =
               currentMainListModel.message.time.toString().transformTime()
       }
      else holder.mainListLastMessageTime.invisible()
        holder.mainListLastMessage.text =
            when (currentMainListModel.message.type) {
                TYPE_TEXT -> currentMainListModel.message.text
                TYPE_FILE -> "Вложенный файл"
                TYPE_VOICE -> "Голосовое сообщение ${
                    currentMainListModel.message.duration.toInt().transformForTimer("mm:ss")
                }"
                TYPE_IMAGE -> "Картинка"
                else -> "Чат удалён"
            }


    }

    override fun getItemCount(): Int {
        return mutableListMainListAdapter.size
    }

    fun addItem(item: MainListModel) {
        var i = -1
        if (mutableListMainListAdapter.any { it.user == item.user && it.message != item.message }) {
            mutableListMainListAdapter.forEachIndexed { index, mainListModel ->
                             if (mainListModel.user == item.user) {
                    i = index
                }
            }
        }
        if (i != -1) {
            notifyItemRemoved(i)
            mutableListMainListAdapter.removeAt(i)
            i = -1
        }
        if (!mutableListMainListAdapter.any { it.user == item.user }) {
            mutableListMainListAdapter.add(item)
            mutableListMainListAdapter.sortByDescending { it.message.time.toString() }
            notifyItemInserted(mutableListMainListAdapter.indexOf(item))
        }
    }

    fun removeDialog(id:String) {
  var i = -1
        mutableListMainListAdapter.forEachIndexed { index, mainListModel ->
            if (mainListModel.user.id == id) {
               i = index
            }
        }
        notifyItemRemoved(i)
        mutableListMainListAdapter.removeAt(i)

    }
}
