package com.android.skillsync.repoistory.Post

import com.android.skillsync.models.Post.Post
import com.android.skillsync.repoistory.ApiManager
import com.google.firebase.Timestamp

class FireStorePostRepository {

    val apiManager = ApiManager()

    companion object {
        const val POSTS_COLLECTION_PATH = "posts"
    }

    fun addPost(post: Post) {
        apiManager.db.collection(POSTS_COLLECTION_PATH).add(post.json)
//            .addOnSuccessListener { callback() }
    }

    fun deletePost(post: Post) {
     //   apiManager.db.collection(POSTS_COLLECTION_PATH).document(postId).delete()
        apiManager.db.collection(POSTS_COLLECTION_PATH).whereEqualTo("id", post.id).get().addOnSuccessListener {
            apiManager.db.collection(POSTS_COLLECTION_PATH).document(it.documents[0].id).delete()
        }
    }

    fun updatePost(postId: String, data: Map<String, Any>, callback: () -> Unit) {
        apiManager.db.collection(POSTS_COLLECTION_PATH).document(postId).update(data)
            .addOnSuccessListener { callback() }
//            .addOnCompleteListener { onFailureCallBack() }
    }

    fun getPostsByOwnerId(ownerId: String, callback: (List<Post>) -> Unit) {
        apiManager.db.collection(POSTS_COLLECTION_PATH)
            .whereEqualTo(Post.OWNER_ID_KEY, ownerId)
            .get().addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val posts: MutableList<Post> = mutableListOf()
                        for (json in it.result) {
                            val post = Post.fromJSON(json.data)
                            posts.add(post)
                        }
                        callback(posts)
                    }
                    false -> callback(listOf())
                }
            }
    }

    fun getPosts(since: Long, callback: (List<Post>) -> Unit) {
        apiManager.db.collection(POSTS_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo(Post.LAST_UPDATED, Timestamp(since, 0))
            .get().addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val posts: MutableList<Post> = mutableListOf()
                        for (json in it.result) {
                            val post = Post.fromJSON(json.data)
                            posts.add(post)
                        }
                        callback(posts)
                    }
                    false -> callback(listOf())
                }
            }
    }
}
