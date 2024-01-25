package com.android.skillsync.repoistory.Post

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android.skillsync.dao.AppLocalDatabase
import com.android.skillsync.models.Post.Post

class LocalStorePostRepository {
    val appLocalDB = AppLocalDatabase
    val postDao = appLocalDB.db.getPostDao()
    val posts: LiveData<MutableList<Post>> = postDao.getAllPosts()

    @WorkerThread
    suspend fun insert(post: Post) {
        postDao.insertPost(post)
    }

    @WorkerThread
    suspend fun update(post: Post) {
        postDao.updatePost(post)
    }

    @WorkerThread
    suspend fun delete(post: Post) {
        postDao.deletePost(post)
    }

    @WorkerThread
    suspend fun deleteAllPosts() {
        postDao.deleteAllPosts()
    }

    @WorkerThread
    fun getAllPosts() {
        postDao.getAllPosts()
    }
}
