package com.android.skillsync.repoistory.Post

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android.skillsync.dao.AppLocalDatabase
import com.android.skillsync.models.Post.Post
import com.android.skillsync.models.Comapny.Company

class LocalStorePostRepository {
    val appLocalDB = AppLocalDatabase
    val postDao = appLocalDB.db.getPostDao()

    @WorkerThread
    fun insert(post: Post) {
        postDao.insertPost(post)
    }

    @WorkerThread
    fun update(post: Post) {
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
    fun getAllPosts(): LiveData<MutableList<Post>> {
        return postDao.getAllPosts()
    }
}
