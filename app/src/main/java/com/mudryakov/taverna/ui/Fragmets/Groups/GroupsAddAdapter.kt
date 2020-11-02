package com.mudryakov.taverna.ui.Fragmets.Groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.downloadAndSetImage
import com.mudryakov.taverna.Objects.invisible
import com.mudryakov.taverna.Objects.visible
import com.mudryakov.taverna.R
import com.mudryakov.taverna.models.CommonModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.group_add_item.view.*


class GroupsAddAdapter : RecyclerView.Adapter<GroupsAddAdapter.GroupAddViewHolder>() {


    class GroupAddViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val baselayout: ConstraintLayout = view.groupAddBaseLayout
        val userPhoto: CircleImageView = view.addGroupUserPhoto
        val userFullName: TextView = view.groupAddUserfullname
        val status: TextView = view.groupAddUserStatus
        val check: CircleImageView = view.groupAddCheck


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupAddViewHolder {
        var join = false
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.group_add_item, parent, false)
        val holder = GroupAddViewHolder(view)
        holder.baselayout.setOnClickListener {
            join = if (!join) {
                holder.check.visible()
                listUsersForGroup.add(mutableListOfallContacts[holder.adapterPosition])
                true
            } else {
                listUsersForGroup.remove(mutableListOfallContacts[holder.adapterPosition])
                holder.check.invisible()
                false
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: GroupAddViewHolder, position: Int) {
        val currentContact = mutableListOfallContacts[position]
        holder.status.text = currentContact.status
        holder.userFullName.text = currentContact.fullName
        holder.userPhoto.downloadAndSetImage(currentContact.photoUrl)
    }

    override fun getItemCount(): Int = mutableListOfallContacts.size


    fun addItem(user: CommonModel) {
        if (mutableListOfallContacts.all { it.id != user.id }) {
            mutableListOfallContacts.add(user)
            notifyItemInserted(mutableListOfallContacts.size)
        }
    }


    companion object {
        var mutableListOfallContacts = mutableListOf<CommonModel>()
        var listUsersForGroup = mutableListOf<CommonModel>()
    }


}


