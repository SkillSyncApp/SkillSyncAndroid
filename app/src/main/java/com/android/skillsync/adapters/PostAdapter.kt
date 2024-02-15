package com.android.skillsync.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.R
import com.android.skillsync.models.Post.Post

class PostAdapter(var posts: MutableList<Post>?) : RecyclerView.Adapter<PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return posts?.size ?: 0
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts?.get(position)

        holder.bind(post)
    }
}
