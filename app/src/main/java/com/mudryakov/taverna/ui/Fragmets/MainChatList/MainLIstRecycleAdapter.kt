package com.mudryakov.taverna.ui.Fragmets.MainChatList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.changeFragment
import com.mudryakov.taverna.Objects.downloadAndSetImage
import com.mudryakov.taverna.Objects.transformForTimer
import com.mudryakov.taverna.Objects.transformTime
import com.mudryakov.taverna.R
import com.mudryakov.taverna.appDatabaseHelper.TYPE_FILE
import com.mudryakov.taverna.appDatabaseHelper.TYPE_IMAGE
import com.mudryakov.taverna.appDatabaseHelper.TYPE_TEXT
import com.mudryakov.taverna.appDatabaseHelper.TYPE_VOICE
import com.mudryakov.taverna.models.MainListModel
import com.mudryakov.taverna.ui.Fragmets.SingleChat.SingleChatFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.main_list_item.view.*

class MainLIstRecycleAdapter : RecyclerView.Adapter<MainLIstRecycleAdapter.MainListViewHolder>() {

    var mutableList = mutableListOf<MainListModel>()


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
        return MainListViewHolder(view)

    }

    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        val currentMainListModel = mutableList[position]
        holder.mainListFullName.text = currentMainListModel.user.fullName
        holder.mainListPhoto.downloadAndSetImage(currentMainListModel.user.photoUrl)
        holder.mainListLastMessageTime.text =
            currentMainListModel.message.time.toString().transformTime()
        holder.mainListLastMessage.text =
            when (currentMainListModel.message.type) {
                TYPE_TEXT -> currentMainListModel.message.text
                TYPE_FILE -> "Вложенный файл"
                TYPE_VOICE -> "Голосовое сообщение ${
                    currentMainListModel.message.duration.toInt().transformForTimer("mm:ss")
                }"
                TYPE_IMAGE -> "Картинка"
                else -> "ошибка"
            }
        holder.layout1.setOnClickListener {
            changeFragment(SingleChatFragment(currentMainListModel.user))
        }
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    fun addItem(item: MainListModel) {
        if (!mutableList.any { it.user == item.user }) {
            mutableList.add(item)
            mutableList.sortByDescending { it.message.time.toString() }
            notifyItemInserted(mutableList.indexOf(item))
        }
    }
}