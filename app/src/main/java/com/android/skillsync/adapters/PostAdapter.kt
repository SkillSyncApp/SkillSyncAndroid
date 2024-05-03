package com.android.skillsync.adapters

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.FeedFragment
import com.android.skillsync.R
import com.android.skillsync.ViewModel.PostViewModel
import com.android.skillsync.domain.UserUseCases
import com.android.skillsync.models.Post.Post
import com.android.skillsync.repoistory.Post.FireStorePostRepository
import com.squareup.picasso.Picasso

class PostAdapter(var posts: MutableList<Post>, var isFromFeed: Boolean) : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    var listener: FeedFragment.OnPostClickListener? = null

    fun setOnPostClickListener(listener: FeedFragment.OnPostClickListener) {
        this.listener = listener
    }

    class PostHolder(
        itemView: View,
        private val posts: List<Post>,
        val listener: FeedFragment.OnPostClickListener?,
        isFromFeed: Boolean
    ) : RecyclerView.ViewHolder(itemView) {
        val ownerNameLabel: TextView = itemView.findViewById(R.id.ownerName)
        val contentLabel: TextView = itemView.findViewById(R.id.content)
        val image = itemView.findViewById<ImageView>(R.id.imagePost)
        val edit = itemView.findViewById<ImageView>(R.id.post_edit_button)
        val deletePostButton = itemView.findViewById<ImageView>(R.id.deletePostButton)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && isFromFeed) {
                    listener?.onPostClicked(posts[position])
                    Log.d("onClickPost", posts[position].ownerId)
                }
            }
            if (!isFromFeed) {
                edit.visibility = View.VISIBLE
                edit.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val args = Bundle()
                        args.putString("postId", posts[position].id)

                        Navigation.findNavController(it)
                            .navigate(R.id.action_profileFragment_to_editPost, args)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostHolder(view, posts, listener, isFromFeed)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = posts[position]

        val currentUserID = UserUseCases().getUserId()
        val isOwner = currentUserID == post.ownerId

        if (isOwner) {
            holder.deletePostButton.visibility = View.VISIBLE
            holder.deletePostButton.setOnClickListener {
                deletePost(post)
            }
        } else {
            holder.deletePostButton.visibility = View.GONE
        }


        if (post.imagePath.isNotBlank()) {
            holder.image.setVisibility(View.VISIBLE)
            val imageUri = Uri.parse(post.imagePath)
            loadImage(imageUri, holder.image)
        } else {
            holder.image.visibility = View.GONE
        }
        holder.ownerNameLabel.text = post.title
        holder.contentLabel.text = post.content
    }

    private fun loadImage(imageUri: Uri, imageView: ImageView) {
        Picasso.get().load(imageUri).into(imageView)
    }


    fun clear() {
        posts.clear()
    }

    fun addAll(newPosts: MutableList<Post>) {
        newPosts.sortByDescending { it.lastUpdated };
        posts.addAll(newPosts)
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }

    private fun deletePost(post: Post) {
        Log.e("PostAdapter", "delete post")
        PostViewModel().deletePost(post)
    }
}
