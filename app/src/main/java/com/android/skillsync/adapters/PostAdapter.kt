package com.android.skillsync.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.R
import com.android.skillsync.models.Post.Post
import com.squareup.picasso.Picasso
import android.net.Uri

class PostAdapter(var posts: List<Post>)
    : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    class PostHolder(itemView:View): RecyclerView.ViewHolder(itemView) { // check if we need to split to a different file - check tal github
        val ownerNameLabel: TextView = itemView.findViewById(R.id.ownerName)
        val contentLabel: TextView = itemView.findViewById(R.id.content)
        val image = itemView.findViewById<ImageView>(R.id.ownerImage)
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
//        if (post.imagePath.isNotEmpty()) {
//            val imageUri = Uri.parse(post.imagePath)
//            loadImage(imageUri, holder.image)
//        }
        holder.ownerNameLabel.text = post.title

        holder.contentLabel.text = post.content
    }

    private fun loadImage(imageUri: Uri, imageView: ImageView) {
        Picasso.get().load(imageUri).into(imageView)
    }


//        Picasso.get()
//            .load(imageUri)
//            .into(imageView, object : Callback {
//                override fun onSuccess() {
//                    // Image loaded successfully
//                    println("Image loaded successfully")
//                }
//
//                override fun onError(e: Exception?) {
//                    // Error loading image, handle it here
//                    println("Error loading image: ${e?.message}")
//                }
//            })
//    }
}
