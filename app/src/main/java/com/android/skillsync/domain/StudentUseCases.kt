package com.android.skillsync.domain

import androidx.lifecycle.LiveData
import com.android.skillsync.models.Student.Student
import com.android.skillsync.repoistory.Auth.FireStoreAuthRepository
import com.android.skillsync.repoistory.Student.FireStoreStudentRepository
import com.android.skillsync.repoistory.Student.LocalStoreUserRepository
import kotlin.random.Random

class StudentUseCases {

    val localStoreStudentRepository: LocalStoreUserRepository = LocalStoreUserRepository()
    val fireStoreStudentRepository: FireStoreStudentRepository = FireStoreStudentRepository()
    val fireStoreAuthRepository: FireStoreAuthRepository = FireStoreAuthRepository()

    val studentLiveData: LiveData<List<Student>> get() = localStoreStudentRepository.student

    fun getAllStudents() {
        studentLiveData
    }

    suspend fun addStudent(student: Student) {
        val studentId = fireStoreStudentRepository.addStudent(student)
        student.id = studentId
        localStoreStudentRepository.addStudent(student)
    }

    suspend fun update(student: Student) {
        localStoreStudentRepository.updateStudent(student)
    }
}
