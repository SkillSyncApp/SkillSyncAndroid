package com.android.skillsync.domain

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.skillsync.R
import com.android.skillsync.repoistory.Auth.FireStoreAuthRepository

class UserUseCases {
    private val fireStoreAuthRepository: FireStoreAuthRepository = FireStoreAuthRepository()
    fun createUser(email: String, password: String, onSuccessCallBack: (String?) -> Unit, onFailureCallBack: (String?) -> Unit) {
        fireStoreAuthRepository.createUser(email, password, { userId ->
            onSuccessCallBack(userId)
        }, onFailureCallBack)
    }
    fun signInUser(email: String, password: String, onSuccessCallBack: () -> Unit, onFailureCallBack: (String?) -> Unit) {
        fireStoreAuthRepository.signInUser(email, password, onSuccessCallBack, onFailureCallBack)
    }

    fun logOutUser() {
        fireStoreAuthRepository.logOutUser()
    }

    fun resetPassword(email: String, onSuccessCallBack: () -> Unit, onFailureCallBack: (String?) -> Unit) {
        fireStoreAuthRepository.resetPassword(email, onSuccessCallBack, onFailureCallBack)
    }

    fun getUserId(): String? {
        return fireStoreAuthRepository.firebaseAuth.currentUser?.uid
    }
    fun setMenuByUserType(userId: String, fragment: Fragment) {
        return fireStoreAuthRepository.getUserType(userId) { userType ->
            if (userType != null) {
                val navController = Navigation.findNavController(fragment.requireView())
                if (userType == "COMPANY") {
                    navController.navigate(R.id.action_groupProfileFragment_to_companyProfileFragment)
                }
            }
        }
    }
}
