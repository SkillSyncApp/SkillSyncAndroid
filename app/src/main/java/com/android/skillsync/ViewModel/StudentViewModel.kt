package com.android.skillsync.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.skillsync.domain.StudentUseCases
import com.android.skillsync.models.Student.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentViewModel: ViewModel() {
    val studentUseCases: StudentUseCases = StudentUseCases()

    fun createUserAsStudent(email: String, password: String, onSuccessCallBack: () -> Unit, onFailureCallBack: (String?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        studentUseCases.fireStoreAuthRepository.createUser(email, password, onSuccessCallBack, onFailureCallBack)
    }

    fun getAllStudents() = viewModelScope.launch(Dispatchers.IO) {
        studentUseCases.getAllStudents()
    }

    fun addStudent(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        studentUseCases.addStudent(student)
    }

    fun update(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        studentUseCases.update(student)
    }
}
