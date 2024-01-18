package com.android.skillsync.repoistory.Post

import com.android.skillsync.models.Post.Post
import com.android.skillsync.repoistory.ApiManager
import java.time.LocalDateTime

class FireStorePostRepository {

    val apiManager = ApiManager()

    companion object {
        const val POSTS_COLLECTION_PATH = "posts"
    }

    fun addPost(post: Post, onSuccessCallBack: () -> Unit, onFailureCallBack: () -> Unit) {
        apiManager.db.collection(POSTS_COLLECTION_PATH).add(post.json)
            .addOnSuccessListener { onSuccessCallBack() }
            .addOnCompleteListener { onFailureCallBack() }
    }

    fun deletePost(postId: String, onSuccessCallBack: () -> Unit, onFailureCallBack: () -> Unit) {
        apiManager.db.collection(POSTS_COLLECTION_PATH).document(postId).delete()
            .addOnSuccessListener { onSuccessCallBack() }
            .addOnCompleteListener { onFailureCallBack() }
    }

    fun updatePost(postId: String, data: Map<String, Any>, onSuccessCallBack: () -> Unit, onFailureCallBack: () -> Unit) {
        apiManager.db.collection(POSTS_COLLECTION_PATH).document(postId).update(data)
            .addOnSuccessListener { onSuccessCallBack() }
            .addOnCompleteListener { onFailureCallBack() }
    }

    fun getPosts(postId: String, since: LocalDateTime, onSuccessCallBack: () -> Unit, onFailureCallBack: () -> Unit) {
        apiManager.db.collection(POSTS_COLLECTION_PATH).whereGreaterThanOrEqualTo(Post.UPDATED_DATE_KEY, since).get()
            .addOnSuccessListener { onSuccessCallBack() }
            .addOnCompleteListener { onFailureCallBack() }
    }
}
