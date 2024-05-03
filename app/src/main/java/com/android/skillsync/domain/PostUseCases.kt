package com.android.skillsync.domain

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.skillsync.models.Post.Post
import com.android.skillsync.repoistory.Post.FireStorePostRepository
import com.android.skillsync.repoistory.Post.LocalStorePostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.concurrent.Executors
import androidx.lifecycle.MediatorLiveData

class PostUseCases {
    val localStorePostRepository: LocalStorePostRepository = LocalStorePostRepository()
    val fireStorePostRepository: FireStorePostRepository = FireStorePostRepository()
    private val coroutineScope = CoroutineScope(Job())

    private var executor = Executors.newSingleThreadExecutor()
    private val _posts: MutableLiveData<List<Post>> = MutableLiveData()

    val posts: LiveData<List<Post>> get() = _posts
    private val mediatorLiveData = MediatorLiveData<List<Post>>()

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

    fun getPostById(id: String): LiveData<Post> {
        return localStorePostRepository.getPostById(id)
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
        fireStorePostRepository.deletePost(post)
        localStorePostRepository.delete(post)
    }

    fun deleteAll() {
        localStorePostRepository.deleteAllPosts()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun update(postId: String, data: Map<String,Any>) {
        fireStorePostRepository.updatePost(postId, data) { updatedPost ->
            if(updatedPost != null) {
                updateLocalCache(updatedPost)
            }
        }
    }

    private var isUpdatingCache = false

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateLocalCache(updatedPost: Post) {
        // Check if cache update is already in progress
        if (isUpdatingCache) {
            return
        }
        isUpdatingCache = true

        val localPostsLiveData = localStorePostRepository.getAllPosts()

        mediatorLiveData.addSource(localPostsLiveData) { localPosts ->
                // Once the LiveData emits a value, perform your operations inside the observer
                val index = localPosts.indexOfFirst { it.id == updatedPost.id }

                try {
                    if (index != -1) {
                        localPosts[index] = updatedPost
                        coroutineScope.launch {
                            // Update the post in the local store
                            localStorePostRepository.update(updatedPost)
                            Post.lastUpdated = Instant.now().epochSecond
                            isUpdatingCache = false
                        }
                    }
                } catch(err: Exception) {
                    println(err.message)
                    Log.d("Cache", err.message.toString())
                }
            }
        }
}