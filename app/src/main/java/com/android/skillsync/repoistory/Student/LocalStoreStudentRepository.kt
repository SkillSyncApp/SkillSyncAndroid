package com.android.skillsync.repoistory.Student

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android.skillsync.dao.AppLocalDatabase
import com.android.skillsync.models.Student.Student

class LocalStoreUserRepository {
    val appLocalDB = AppLocalDatabase
    val studentDao = appLocalDB.db.getStudentDao()
    val student: LiveData<MutableList<Student>> = studentDao.getAllStudents()

    @WorkerThread
    suspend fun addStudent(student: Student) {
        studentDao.addStudent(student)
    }

    @WorkerThread
    suspend fun updateStudent(student: Student) {
        studentDao.updateStudent(student)
    }

    @WorkerThread
    suspend fun deleteAllStudents() {
        studentDao.deleteAllStudents()
    }
}
