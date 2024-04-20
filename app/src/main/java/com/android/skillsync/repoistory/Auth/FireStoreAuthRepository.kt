package com.android.skillsync.repoistory.Auth

import com.android.skillsync.repoistory.ApiManager

class FireStoreAuthRepository {

    val firebaseAuth = ApiManager().firebaseAuth

    fun createUser(email: String, password: String, onSuccessCallBack: (String?) -> Unit, onFailureCallBack: (String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid
                onSuccessCallBack(userId)
            }
            .addOnFailureListener { e ->
                onFailureCallBack(e.message)
            }
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
