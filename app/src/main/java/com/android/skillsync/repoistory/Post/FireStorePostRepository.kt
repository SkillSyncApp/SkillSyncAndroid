package com.android.skillsync.repoistory.Post

import com.android.skillsync.models.Post.Post
import com.android.skillsync.repoistory.ApiManager
import com.google.firebase.Timestamp

class FireStorePostRepository {

    val apiManager = ApiManager()

    companion object {
        const val POSTS_COLLECTION_PATH = "posts"
    }

    var onSuccess: (() -> Unit)? = null
    var onFailure: (() -> Unit)? = null

    fun addPost(post: Post, callback: () -> Unit) {
        apiManager.db.collection(POSTS_COLLECTION_PATH).add(post.json)
            .addOnSuccessListener { callback() }
            .addOnCompleteListener { onFailure?.invoke() }
    }

    fun deletePost(postId: String) {
        apiManager.db.collection(POSTS_COLLECTION_PATH).document(postId).delete()
            .addOnSuccessListener { onSuccess?.invoke() }
            .addOnCompleteListener { onFailure?.invoke() }
    }

    fun updatePost(postId: String, data: Map<String, Any>) {
        apiManager.db.collection(POSTS_COLLECTION_PATH).document(postId).update(data)
            .addOnSuccessListener { onSuccess?.invoke() }
            .addOnCompleteListener { onFailure?.invoke() }
    }

    fun getPosts(since: Long, callback: (List<Post>) -> Unit) {
        apiManager.db.collection(POSTS_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo(Post.LAST_UPDATED, Timestamp(since, 0)).get()
            .addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val posts: MutableList<Post> = mutableListOf()
                        for (json in it.result) {
                            val post = Post.fromJSON(json.data)
                            posts.add(post)
                        }
                    }
                    false -> callback(listOf())
                }
            }
    }
}
