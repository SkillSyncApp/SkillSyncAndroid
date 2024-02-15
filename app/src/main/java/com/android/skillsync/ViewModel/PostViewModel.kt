package com.android.skillsync.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.skillsync.domain.PostUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.android.skillsync.models.Post.Post
import kotlinx.coroutines.launch

class PostViewModel: ViewModel() {
    private val _posts = MutableLiveData<MutableList<Post>>()
    var posts: LiveData<MutableList<Post>> get() = _posts

    private val postsUseCases: PostUseCases = PostUseCases()

    init {
        posts = _posts
    }

    fun getPostsLiveData(callback: (LiveData<MutableList<Post>>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val postsData = postsUseCases.getAllPosts()
                withContext(Dispatchers.Main) {
                    // Update LiveData on the main thread
                    _posts.postValue(postsData.value)
                    callback(_posts)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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

    fun refreshPosts() = viewModelScope.launch(Dispatchers.IO) {
        postsUseCases.refreshPosts()
    }

    fun update(post: Post, data: Map<String, Any>) = viewModelScope.launch(Dispatchers.IO) {
        postsUseCases.update(post, data)
    }
}
