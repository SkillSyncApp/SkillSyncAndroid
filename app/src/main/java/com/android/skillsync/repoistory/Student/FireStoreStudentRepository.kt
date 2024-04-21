package com.android.skillsync.repoistory.Student

import android.util.Log
import com.android.skillsync.models.Student.Student
import com.android.skillsync.models.Type
import com.android.skillsync.models.UserType
import com.android.skillsync.repoistory.ApiManager
import com.google.firebase.Timestamp
import kotlinx.coroutines.tasks.await
import com.android.skillsync.repoistory.Auth.FireStoreAuthRepository.Companion.USER_TYPE_COLLECTION_PATH

class FireStoreStudentRepository {

    companion object {
        const val USERS_COLLECTION_PATH = "users"
    }

    val apiManager = ApiManager()

    suspend fun addStudent(student: Student, onSuccess: (String) -> Unit): String {

        val documentReference = apiManager.db.collection(USERS_COLLECTION_PATH)
            .add(student.json)
            .await()

        onSuccess(student.id)
        return documentReference.id
    }

    fun getStudent(studentId: String, callback: (student: Student) -> Unit) {
        val studentDocument = apiManager.db.collection(USERS_COLLECTION_PATH)

        studentDocument.whereEqualTo("id", studentId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.isEmpty) {
                    val data = documentSnapshot.documents[0].data;

                    if (data != null) {
                        if (data.isNotEmpty()) {
                            val timestamp: Timestamp? = data["lastUpdated"] as? Timestamp
                            var lastUpdated: Long? = null
                            timestamp?.let {
                                lastUpdated = it.seconds
                            }

                            val student = Student(
                                name = data["name"] as String,
                                email = data["email"] as String,
                                bio = data["bio"] as String,
                                institution = data["institution"] as String,
                                image = data["image"] as String,
                                lastUpdated = lastUpdated ?: 0
                            )

                            callback(student)
                        }
                    }
                }
            }
            .addOnFailureListener { locationException ->
                Log.e("Firestore", "Error getting student: $locationException")
            }
    }

    fun setStudentInUserTypeDB(companyId: String) = run {
        val userType = UserType(Type.STUDENT)
        apiManager.db.collection(USER_TYPE_COLLECTION_PATH).document(companyId).set(userType)
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
