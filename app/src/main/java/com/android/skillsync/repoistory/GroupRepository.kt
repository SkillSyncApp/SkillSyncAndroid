package com.android.skillsync.repoistory

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android.skillsync.dao.GroupDao
import com.android.skillsync.models.Group.Group

class GroupRepository(private val groupDao: GroupDao) {
    val group: LiveData<List<Group>> = groupDao.getAllGroups()

    @WorkerThread
    suspend fun insert(group: Group) {
        groupDao.insert(group)
    }

    @WorkerThread
    suspend fun update(group: Group) {
        groupDao.update(group)
    }

    @WorkerThread
    suspend fun delete(group: Group) {
        groupDao.delete(group)
    }

    @WorkerThread
    suspend fun deleteAllGroups() {
        groupDao.deleteAllGroups()
    }

    @WorkerThread
    fun getAllPosts() {
        groupDao.getAllGroups()
    }
}
