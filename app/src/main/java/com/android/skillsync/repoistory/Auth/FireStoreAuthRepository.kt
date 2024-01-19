package com.android.skillsync.repoistory.Auth

import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.skillsync.R
import com.android.skillsync.models.Student.Student
import com.android.skillsync.repoistory.ApiManager

class FireStoreAuthRepository {

    val firebaseAuth = ApiManager().firebaseAuth

    fun createUser(email: String, password: String, onSuccessCallBack: () -> Unit, onFailureCallBack: (String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccessCallBack() }
            .addOnFailureListener { onFailureCallBack(it.message) }
    }

    fun signInUser(email: String, password: String, onSuccessCallBack: () -> Unit, onFailureCallBack: (String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccessCallBack() }
            .addOnFailureListener { onFailureCallBack(it.message) }
    }

    fun logOutUser() {
        firebaseAuth.signOut()
    }

    fun resetPassword(email: String, onSuccessCallBack: () -> Unit, onFailureCallBack: (String?) -> Unit) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onSuccessCallBack() }
            .addOnFailureListener { onFailureCallBack(it.message) }
    }
}
