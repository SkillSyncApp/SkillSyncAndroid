package com.android.skillsync.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.R
import com.android.skillsync.models.Group.FirebaseGroup

class GroupMemberAdapter(private val membersList: List<FirebaseGroup>)
    : RecyclerView.Adapter<GroupMemberAdapter.GroupMemberHolder>() {

    class GroupMemberHolder(itemView:View): RecyclerView.ViewHolder(itemView) { // check if we need to split to a different file - check tal github
        val memberNameLabel: TextView = itemView.findViewById(R.id.userName);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMemberHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.member_list_item, parent, false);
        return GroupMemberHolder(view);
    }

    override fun getItemCount(): Int {
        return membersList.size;
    }

    override fun onBindViewHolder(holder: GroupMemberHolder, position: Int) {
        val member = membersList[position];
        holder.memberNameLabel.text = member.name;
    }
}