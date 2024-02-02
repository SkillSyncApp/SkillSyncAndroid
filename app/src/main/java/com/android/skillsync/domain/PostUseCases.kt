package com.android.skillsync.domain

import androidx.lifecycle.LiveData
import com.android.skillsync.models.Post.Post
import com.android.skillsync.models.Student.Student
import com.android.skillsync.repoistory.Auth.FireStoreAuthRepository
import com.android.skillsync.repoistory.Post.FireStorePostRepository
import com.android.skillsync.repoistory.Post.LocalStorePostRepository
import com.android.skillsync.repoistory.Student.FireStoreStudentRepository
import com.android.skillsync.repoistory.Student.LocalStoreUserRepository

class PostUseCases() {

    val localStorePostRepository: LocalStorePostRepository = LocalStorePostRepository()
    val fireStorePostRepository: FireStorePostRepository = FireStorePostRepository()
    val fireStoreAuthRepository: FireStoreAuthRepository = FireStoreAuthRepository()


    val postsLiveData: LiveData<MutableList<Post>> get() = localStorePostRepository.posts

    fun getAll() {
        postsLiveData
    }

    suspend fun add(post: Post) {
        localStorePostRepository.addPost(post)
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