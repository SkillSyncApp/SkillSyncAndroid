package com.android.skillsync.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.skillsync.models.Post.Post
import com.android.skillsync.repoistory.Post.LocalStorePostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewModel(private val postRepository: LocalStorePostRepository): ViewModel() {

    // LiveData to observe changes in the list of posts
    private val _postsLiveData = postRepository.posts
    val postsLiveData: LiveData<List<Post>> get() = _postsLiveData

    fun getAllPosts() = viewModelScope.launch(Dispatchers.IO) {
        postRepository.getAllPosts()
    }

    fun addPost(post: Post) = viewModelScope.launch(Dispatchers.IO) {
        postRepository.insert(post)
    }

    fun deletePost(post: Post) = viewModelScope.launch(Dispatchers.IO) {
        postRepository.delete(post)
    }

    fun deleteAllPosts() = viewModelScope.launch(Dispatchers.IO) {
        postRepository.deleteAllPosts()
    }

    fun update(post: Post) = viewModelScope.launch(Dispatchers.IO) {
        postRepository.update(post)
    }
}
