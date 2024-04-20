package com.android.skillsync.domain

import androidx.lifecycle.LiveData
import com.android.skillsync.models.Student.Student
import com.android.skillsync.repoistory.Auth.FireStoreAuthRepository
import com.android.skillsync.repoistory.Student.FireStoreStudentRepository
import com.android.skillsync.repoistory.Student.LocalStoreUserRepository

class StudentUseCases {

    val localStoreStudentRepository: LocalStoreUserRepository = LocalStoreUserRepository()
    val fireStoreStudentRepository: FireStoreStudentRepository = FireStoreStudentRepository()
    val fireStoreAuthRepository: FireStoreAuthRepository = FireStoreAuthRepository()

    val studentLiveData: LiveData<MutableList<Student>> get() = localStoreStudentRepository.student

    fun getAllStudents() {
        studentLiveData
    }

    fun getStudent(studentId: String, callback: (student: Student) -> Unit) {
        fireStoreStudentRepository.getStudent(studentId, callback);
    }

    suspend fun addStudent(student: Student) {
        fireStoreStudentRepository.addStudent(student)
        localStoreStudentRepository.addStudent(student) // todo maybe remove
    }

    suspend fun update(student: Student) {
        localStoreStudentRepository.updateStudent(student)
    }
}
