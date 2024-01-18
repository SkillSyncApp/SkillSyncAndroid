package com.android.skillsync.repoistory.Student

import android.widget.Toast
import androidx.navigation.Navigation
import com.android.skillsync.R
import com.android.skillsync.models.Student.Student
import com.android.skillsync.repoistory.ApiManager
import com.google.firebase.auth.FirebaseAuth

class FireStoreStudentRepository {

    companion object {
        const val USERS_COLLECTION_PATH = "users"
    }

    val apiManager = ApiManager()

    fun addStudent(student: Student, /*onSuccessCallBack: () -> Unit, onFailureCallBack: () -> Unit*/){
        apiManager.db.collection(USERS_COLLECTION_PATH).document(student.id.toString()).set(student.json)
            .addOnSuccessListener { /*onSuccessCallBack()*/ }
            .addOnFailureListener {  /*onFailureCallBack()*/ }
    }

    fun updateStudent(student: Student, data: Map<String, Any>, onSuccessCallBack: () -> Unit, onFailureCallBack: () -> Unit){
        apiManager.db.collection(USERS_COLLECTION_PATH).document(student.id.toString()).update(data)
            .addOnSuccessListener { onSuccessCallBack() }
            .addOnFailureListener { onFailureCallBack() }
    }

    fun deleteStudent(student: Student, onSuccessCallBack: () -> Unit, onFailureCallBack: () -> Unit){
        apiManager.db.collection(USERS_COLLECTION_PATH).document(student.id.toString()).delete()
            .addOnSuccessListener { onSuccessCallBack() }
            .addOnFailureListener { onFailureCallBack() }
    }
}
