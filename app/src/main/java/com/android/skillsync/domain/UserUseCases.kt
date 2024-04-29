package com.android.skillsync.domain

import com.android.skillsync.models.Post.Post
import com.android.skillsync.models.UserInfo
import com.android.skillsync.repoistory.Auth.FireStoreAuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserUseCases {
    private val fireStoreAuthRepository: FireStoreAuthRepository = FireStoreAuthRepository()
    private val studentUseCases: StudentUseCases = StudentUseCases()
    private val companyUseCases: CompanyUseCases = CompanyUseCases()
    private val postUseCases: PostUseCases = PostUseCases()

    fun signInUser(email: String, password: String, onSuccessCallBack: () -> Unit, onFailureCallBack: (String?) -> Unit) {
        fireStoreAuthRepository.signInUser(email, password, onSuccessCallBack, onFailureCallBack)
    }

    fun logOutUser() {
        fireStoreAuthRepository.logOutUser()
        Post.lastUpdated = 0
        CoroutineScope(Dispatchers.IO).launch {
            postUseCases.deleteAll();
        }
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
}
