package com.android.skillsync.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skillsync.models.Post.Post
import com.android.skillsync.repoistory.Post.FireStorePostRepository
import com.android.skillsync.repoistory.Post.LocalStorePostRepository
import java.util.concurrent.Executors
import kotlin.collections.indexOfFirst

class PostUseCases {

    val localStorePostRepository: LocalStorePostRepository = LocalStorePostRepository()
    val fireStorePostRepository: FireStorePostRepository = FireStorePostRepository()

    private var executor = Executors.newSingleThreadExecutor()
    val posts: MutableLiveData<MutableList<Post>> = MutableLiveData()

    fun getAllPosts(): LiveData<MutableList<Post>> {
        refreshPosts()
        if (posts.value == null) {
            posts.value = localStorePostRepository.getAllPosts().value
        }
        return posts
    }

    fun add(post: Post) {
        fireStorePostRepository.addPost(post) {
            refreshPosts()
        }
    }

    fun refreshPosts() {
        // 1. Get last local update
        val lastUpdated: Long = Post.lastUpdated

        // 2. Get all updated records from firestore since last update locally
        fireStorePostRepository.getPosts(lastUpdated) { posts ->
            Log.i("TAG", "Firebase returned ${posts.size}, lastUpdated: $lastUpdated")
            // 3. Insert new record to ROOM
            executor.execute {
                var time = lastUpdated
                for (post in posts) {
                    localStorePostRepository.insert(post)
                    post.lastUpdated?.let {
                        if (time < it)
                            time = post.lastUpdated ?: System.currentTimeMillis()
                    }

                    // 4. Update local data
                    Post.lastUpdated = time
                }
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