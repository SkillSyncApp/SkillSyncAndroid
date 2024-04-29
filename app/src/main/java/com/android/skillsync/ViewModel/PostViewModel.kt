package com.android.skillsync.ViewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.skillsync.domain.PostUseCases
import com.android.skillsync.models.Post.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostViewModel: ViewModel() {

    private val _posts: MutableLiveData<MutableList<Post>> = MutableLiveData()
    val posts: LiveData<MutableList<Post>> = _posts
//    enum class LoadingState {
//        LOADING,
//        LOADED
//    }
//    val postsListLoadingState: MutableLiveData<LoadingState> = MutableLiveData(LoadingState.LOADED)

    private val postsUseCases: PostUseCases = PostUseCases()

    init {
        postsUseCases.posts.observeForever { posts ->
            _posts.postValue(posts.toMutableList())
        }
    }

    fun getPosts() = viewModelScope.launch (Dispatchers.IO) {
        postsUseCases.posts
    }

    fun getPostsByOwnerId(ownerId: String, callback: (posts: List<Post>) -> Unit) = viewModelScope.launch (Dispatchers.IO) {
        postsUseCases.getPostsByOwnerId(ownerId, callback);
    }

    fun getPostById(id: String) = viewModelScope.launch (Dispatchers.IO) {
        postsUseCases.getPostById(id)
    }

   fun addPost(post: Post, callback: (Boolean) -> Unit) {
       viewModelScope.launch(Dispatchers.IO) {
           try {
               postsUseCases.add(post)
               withContext(Dispatchers.Main) {
                   callback(true)
               }
           } catch (e: Exception) {
               withContext(Dispatchers.Main) {
                   callback(false)
               }
           }
       }
   }


    fun deletePost(post: Post) = viewModelScope.launch(Dispatchers.IO) {
        postsUseCases.delete(post)
    }

    fun deleteAllPosts() = viewModelScope.launch(Dispatchers.IO) {
        postsUseCases.deleteAll()
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
