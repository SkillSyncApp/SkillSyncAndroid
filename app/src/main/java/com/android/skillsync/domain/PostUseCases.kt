package com.android.skillsync.domain

import android.util.Log
import androidx.lifecycle.LiveData
import com.android.skillsync.models.Post.Post
import com.android.skillsync.repoistory.Post.FireStorePostRepository
import com.android.skillsync.repoistory.Post.LocalStorePostRepository
import java.util.concurrent.Executors

class PostUseCases {

    val localStorePostRepository: LocalStorePostRepository = LocalStorePostRepository()
    val fireStorePostRepository: FireStorePostRepository = FireStorePostRepository()

    private var executor = Executors.newSingleThreadExecutor()
    val posts: LiveData<MutableList<Post>>?= null

    fun getAll(): LiveData<MutableList<Post>>  {
        refreshPosts()
        return posts ?: localStorePostRepository.getAllPosts()
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
                    localStorePostRepository.add(post)

                    post.lastUpdated?.let { it ->
                        if (time < it)
                            time = post.lastUpdated ?: System.currentTimeMillis()
                    }
                }

                // 4. Update local data
                Post.lastUpdated = time
            }
        }
    }

    suspend fun add(post: Post) {
        fireStorePostRepository.addPost(post) {
            refreshPosts()
        }
    }

    suspend fun delete(post: Post) {
        localStorePostRepository.delete(post)
    }

    suspend fun deleteAll() {
        localStorePostRepository.deleteAllPosts()
    }

    suspend fun update(post: Post) {
        localStorePostRepository.update(post)
    }
}