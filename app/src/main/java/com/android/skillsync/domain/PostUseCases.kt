package com.android.skillsync.domain

import androidx.lifecycle.LiveData
import com.android.skillsync.models.Post.Post
import com.android.skillsync.repoistory.Post.FireStorePostRepository
import com.android.skillsync.repoistory.Post.LocalStorePostRepository

class PostUseCases(
    private val localStorePostRepository: LocalStorePostRepository,
    private val fireStorePostRepository: FireStorePostRepository) {

    val postsLiveData: LiveData<List<Post>> get() = localStorePostRepository.posts

    suspend fun getAllPosts() {
        localStorePostRepository.getAllPosts()
    }

    suspend fun addPost(post: Post) {
        localStorePostRepository.insert(post)
    }

    suspend fun deletePost(post: Post) {
        localStorePostRepository.delete(post)
    }

    suspend fun deleteAllPosts() {
        localStorePostRepository.deleteAllPosts()
    }

    suspend fun update(post: Post) {
        localStorePostRepository.update(post)
    }
}
