package com.android.skillsync.domain

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skillsync.models.Post.Post
import com.android.skillsync.repoistory.Post.FireStorePostRepository
import com.android.skillsync.repoistory.Post.LocalStorePostRepository
import java.time.Instant
import java.util.concurrent.Executors

class PostUseCases {

    val localStorePostRepository: LocalStorePostRepository = LocalStorePostRepository()
    val fireStorePostRepository: FireStorePostRepository = FireStorePostRepository()

    private var executor = Executors.newSingleThreadExecutor()
    private val _posts: MutableLiveData<List<Post>> = MutableLiveData()

    val posts: LiveData<List<Post>> get() = _posts

    init {
        // Initialize posts by observing local storage
        val localPostsLiveData = localStorePostRepository.getAllPosts()
        localPostsLiveData.observeForever { localPosts ->
            _posts.value = localPosts
        }
    }

    fun add(post: Post) {
        fireStorePostRepository.addPost(post)
    }

    fun getPostsByOwnerId(ownerId: String, callback: (posts: List<Post>) -> Unit) {
        fireStorePostRepository.getPostsByOwnerId(ownerId, callback);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshPosts() {
        // 1. Get last local update
        val lastUpdated: Long = Post.lastUpdated

        // 2. Get all updated records from firestore since last update locally
        fireStorePostRepository.getPosts(lastUpdated) { posts ->
            // 3. Insert new record to ROOM
            executor.execute {
                for (post in posts) {
                    localStorePostRepository.insert(post)

                }

                // 4. Update local data
                Post.lastUpdated = Instant.now().epochSecond
            }
        }
    }

    suspend fun delete(post: Post) {
        localStorePostRepository.delete(post)
    }

    suspend fun deleteAll() {
        localStorePostRepository.deleteAllPosts()
    }

    suspend fun update(post: Post, data: Map<String,Any>) {
        fireStorePostRepository.updatePost(post.id, data) {
            updateLocalCache(post)
        }
    }

    private fun updateLocalCache(updatedPost: Post) {
        val currentPosts = localStorePostRepository.getAllPosts().value

        // Check if currentPosts is not null before proceeding
        currentPosts?.let { posts ->
            // Find the index of the post to be updated in the list
            val index = posts.indexOfFirst { it.id == updatedPost.id }

            if (index != -1) {
                // If the post is found, update it in the local cache
                posts[index] = updatedPost
                localStorePostRepository.update(updatedPost)
            } else {
                // If the post is not found, insert it into the local cache
                localStorePostRepository.insert(updatedPost)
            }

            // Update the last updated timestamp in Post singleton
            Post.lastUpdated = System.currentTimeMillis()
        }
    }
}