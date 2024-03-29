package com.android.skillsync.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Query
import com.android.skillsync.models.Post.Post

@Dao
interface PostDao {

    @Query("SELECT * FROM post")
    fun getAllPosts(): LiveData<MutableList<Post>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertPost(post: Post)

    @Update
    fun updatePost(post: Post)

    @Delete
    fun deletePost(post: Post)

    @Query("DELETE FROM post")
    suspend fun deleteAllPosts()

    @Query("SELECT title FROM Post WHERE id =:id")
    fun getPostTitle(id: String): String

    @Query("SELECT logo FROM Company WHERE id =:id")
    fun getCompanyUrlLogo(id: String): String
}
