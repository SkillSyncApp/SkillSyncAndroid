package com.android.skillsync.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.models.Post.Post

class PostViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView)  {

    var postTitleTextView: TextView? = null
    var postDescription: TextView? = null
    var postImage: ImageView? = null

    var post: Post? = null
    fun bind(post: Post?) {
        this.post = post
        postTitleTextView?.text = post?.title
        postDescription?.text = post?.content
    }
}