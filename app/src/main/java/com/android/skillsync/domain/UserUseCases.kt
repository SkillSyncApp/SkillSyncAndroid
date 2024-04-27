package com.android.skillsync.domain

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.skillsync.R
import com.android.skillsync.repoistory.Auth.FireStoreAuthRepository
import com.android.skillsync.models.UserInfo


class UserUseCases {
    private val fireStoreAuthRepository: FireStoreAuthRepository = FireStoreAuthRepository()
    private val studentUseCases: StudentUseCases = StudentUseCases()
    private val companyUseCases: CompanyUseCases = CompanyUseCases()

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

    fun getUserInfo(id: String, callback: (userInfo: UserInfo?, error: String?) -> Unit) {
        fireStoreAuthRepository.getUserType(id) { userType ->
            when(userType) {
                "STUDENT" -> studentUseCases.getStudent(id) { studentInfo ->
                    callback(UserInfo.UserStudent(studentInfo.copy(id = id)), null)
                }
                "COMPANY" -> companyUseCases.getCompany(id) { companyInfo ->
                    callback(UserInfo.UserCompany(companyInfo.copy(id = id)), null)
                }
                else -> callback(null, "Unknown user type") // Handle other types if needed
            }
        }
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
