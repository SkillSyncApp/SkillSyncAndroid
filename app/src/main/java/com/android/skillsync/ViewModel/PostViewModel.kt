package com.android.skillsync.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.skillsync.domain.PostUseCases
import com.android.skillsync.models.Post.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostViewModel: ViewModel() {

    private val _posts: MutableLiveData<List<Post>> = MutableLiveData()
    val posts: LiveData<List<Post>> = _posts
//    enum class LoadingState {
//        LOADING,
//        LOADED
//    }
//    val postsListLoadingState: MutableLiveData<LoadingState> = MutableLiveData(LoadingState.LOADED)

    private val postsUseCases: PostUseCases = PostUseCases()

    init {
        postsUseCases.posts.observeForever { posts ->
            _posts.value = posts
        }
    }

    fun getPosts() = viewModelScope.launch (Dispatchers.IO) {
        postsUseCases.posts
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

    fun refreshPosts() {
        viewModelScope.launch(Dispatchers.IO) {
//            postsListLoadingState.value = LoadingState.LOADING
            postsUseCases.refreshPosts()
//            postsListLoadingState.value = LoadingState.LOADED
        }
    }

    fun update(post: Post, data: Map<String, Any>) = viewModelScope.launch(Dispatchers.IO) {
        postsUseCases.update(post, data)
    }
}
