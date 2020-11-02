package com.mudryakov.taverna.ui.Fragmets.Groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mudryakov.taverna.Objects.downloadAndSetImage
import com.mudryakov.taverna.Objects.invisible
import com.mudryakov.taverna.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.greate_group_item.view.*


class GroupGreateAdapter : RecyclerView.Adapter<GroupGreateAdapter.GroupGreateViewHolder>() {

    class GroupGreateViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val userPhoto: CircleImageView = view.greateGroupUserPhoto
        val userFullName: TextView = view.groupGreateUserfullname
        val status: TextView = view.groupGreateUserStatus
        val divider: View = view.divider

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupGreateAdapter.GroupGreateViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.greate_group_item, parent, false)
        return GroupGreateViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupGreateViewHolder, position: Int) {
        val currentUser = GroupsAddAdapter.listUsersForGroup[position]

        if (holder.adapterPosition == 0)
            holder.divider.invisible()
        holder.status.text = currentUser.status
        holder.userFullName.text = currentUser.fullName
        holder.userPhoto.downloadAndSetImage(currentUser.photoUrl)
    }

    override fun getItemCount(): Int = GroupsAddAdapter.listUsersForGroup.size


}








