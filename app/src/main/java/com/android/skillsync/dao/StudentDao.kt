package com.android.skillsync.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.skillsync.models.Student.Student

@Dao
interface StudentDao {

    @Query("SELECT * FROM student")
    fun getAllStudents(): LiveData<MutableList<Student>>

    @Insert
    fun addStudent(student: Student)


    @Update
    fun updateStudent(student: Student)

    @Delete
    fun deleteStudent(student: Student)

    @Query("DELETE FROM student")
    suspend fun deleteAllStudents()

}
