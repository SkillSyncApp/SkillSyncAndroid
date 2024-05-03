package com.android.skillsync.repoistory.Post

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android.skillsync.dao.AppLocalDatabase
import com.android.skillsync.models.Post.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalStorePostRepository {
    val appLocalDB = AppLocalDatabase
    val postDao = appLocalDB.db.getPostDao()

    @WorkerThread
    fun insert(post: Post) {
        postDao.insertPost(post)
    }

    @WorkerThread
    suspend fun update(post: Post) {
        withContext(Dispatchers.IO) {
            postDao.updatePost(post)
        }
    }

    @WorkerThread
    suspend fun delete(post: Post) {
        postDao.deletePost(post)
    }

    @WorkerThread
    fun deleteAllPosts() {
        postDao.deleteAllPosts()
    }

    @WorkerThread
    fun getAllPosts(): LiveData<MutableList<Post>> {
        return postDao.getAllPosts()
    }
}
