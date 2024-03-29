package com.android.skillsync.repoistory.Student

import com.android.skillsync.models.Student.Student
import com.android.skillsync.repoistory.ApiManager
import kotlinx.coroutines.tasks.await

class FireStoreStudentRepository {

    companion object {
        const val USERS_COLLECTION_PATH = "users"
    }

    val apiManager = ApiManager()

    suspend fun addStudent(student: Student, /*onSuccessCallBack: () -> Unit, onFailureCallBack: () -> Unit*/): String {

        val documentReference = apiManager.db.collection(USERS_COLLECTION_PATH)
            .add(student.json)
            .await()

        return documentReference.id
    }

    fun updateStudent(student: Student, data: Map<String, Any>, onSuccessCallBack: () -> Unit, onFailureCallBack: () -> Unit){
        apiManager.db.collection(USERS_COLLECTION_PATH).document(student.id).update(data)
            .addOnSuccessListener { onSuccessCallBack() }
            .addOnFailureListener { onFailureCallBack() }
    }

    fun deleteStudent(student: Student, onSuccessCallBack: () -> Unit, onFailureCallBack: () -> Unit){
        apiManager.db.collection(USERS_COLLECTION_PATH).document(student.id).delete()
            .addOnSuccessListener { onSuccessCallBack() }
            .addOnFailureListener { onFailureCallBack() }
    }
}
