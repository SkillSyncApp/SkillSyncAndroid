package com.android.skillsync.ViewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.skillsync.domain.UserUseCases
import com.android.skillsync.models.UserInfo

class UserAuthViewModel: ViewModel() {
    val userUseCases: UserUseCases = UserUseCases()
    private val _userId = MutableLiveData<String?>()
    val userId: LiveData<String?> get() = _userId

    fun signInUser(email: String, password: String, onSuccessCallBack: () -> Unit, onFailureCallBack: (String?) -> Unit) {
        userUseCases.signInUser(email, password, onSuccessCallBack = {
            // Set the user ID in the LiveData upon successful sign-in
            _userId.postValue(userUseCases.getUserId())
            onSuccessCallBack.invoke()
        }, onFailureCallBack)
    }

    fun getUserId(): String? {
        return userId.value
    }

    fun logOutUser() {
        userUseCases.logOutUser()
    }

    fun resetPassword(email: String, onSuccessCallBack: () -> Unit, onFailureCallBack: (String?) -> Unit) {
        userUseCases.resetPassword(email, onSuccessCallBack, onFailureCallBack)
    }
    fun setMenuByUserType(userId: String, fragment: Fragment) {
        userUseCases.setMenuByUserType(userId, fragment)
    }

    fun getInfoOnUser(id: String, onCallBack: (userInfo: UserInfo?, error: String?) -> Unit) {
        userUseCases.getUserInfo(id, onCallBack)
    }
}
