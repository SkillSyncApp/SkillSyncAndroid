package com.android.skillsync.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.skillsync.domain.PostUseCases
import com.android.skillsync.models.Post.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewModel(): ViewModel() {
    val postsUseCases: PostUseCases = PostUseCases()

    fun getAllPosts() = viewModelScope.launch(Dispatchers.IO) {
        postsUseCases.getAll()
    }

    fun addPost(post: Post) = viewModelScope.launch(Dispatchers.IO) {
        postsUseCases.add(post)
    }

    fun deletePost(post: Post) = viewModelScope.launch(Dispatchers.IO) {
        postsUseCases.delete(post)
    }

    fun deleteAllPosts() = viewModelScope.launch(Dispatchers.IO) {
        postsUseCases.deleteAll()
    }

    fun update(post: Post) = viewModelScope.launch(Dispatchers.IO) {
        postsUseCases.update(post)
    }
}
