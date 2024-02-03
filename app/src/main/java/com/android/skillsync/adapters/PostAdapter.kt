package com.android.skillsync.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.R
import com.android.skillsync.models.Post.Post

class PostAdapter(private val posts: List<Post>)
    : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    class PostHolder(itemView:View): RecyclerView.ViewHolder(itemView) { // check if we need to split to a different file - check tal github
        val ownerNameLabel: TextView = itemView.findViewById(R.id.ownerName)
        val contentLabel: TextView = itemView.findViewById(R.id.content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = posts[position]

        holder.ownerNameLabel.text = post.ownerId
        holder.contentLabel.text = post.content
    }
}
