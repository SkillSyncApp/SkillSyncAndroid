package com.android.skillsync.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.skillsync.domain.UserUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserAuthViewModel: ViewModel() {
    val userUseCases: UserUseCases = UserUseCases()

    fun createUser(email: String, password: String, onSuccessCallBack: () -> Unit, onFailureCallBack: (String?) -> Unit) = viewModelScope.launch(
        Dispatchers.IO) {
        userUseCases.createUser(email, password, onSuccessCallBack, onFailureCallBack)
    }


    fun signInUser(email: String, password: String, onSuccessCallBack: () -> Unit, onFailureCallBack: (String?) -> Unit) {
        userUseCases.signInUser(email, password, onSuccessCallBack, onFailureCallBack)
    }

    fun logOutUser() {
        userUseCases.logOutUser()
    }

    fun resetPassword(email: String, onSuccessCallBack: () -> Unit, onFailureCallBack: (String?) -> Unit) {
        userUseCases.resetPassword(email, onSuccessCallBack, onFailureCallBack)
    }
}
