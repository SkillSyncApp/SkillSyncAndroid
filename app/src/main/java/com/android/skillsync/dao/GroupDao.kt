package com.android.skillsync.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.skillsync.models.Group.Group

@Dao
interface GroupDao {
    @Query("SELECT * FROM `group`")
    fun getAllGroups(): LiveData<List<Group>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(group: Group)

    @Delete
    suspend fun delete(group: Group)

    @Query("DELETE FROM `group`")
    suspend fun deleteAllGroups()

    @Update
    suspend fun update(group: Group)

    @Query("SELECT name FROM `Group` WHERE id =:id")
    suspend fun getGroupName(id: String): String

    @Query("SELECT email FROM `group` WHERE id =:id")
    suspend fun getGroupEmail(id: String): String

    @Query("SELECT institution FROM `group` WHERE id =:id")
    suspend fun getGroupInstitution(id: String): String

    @Query("SELECT memberNames FROM `group` WHERE id =:id")
    suspend fun getGroupTeamMembers(id: String): List<String>
}
