package com.android.skillsync.models

import com.android.skillsync.models.Comapny.Company
import com.android.skillsync.models.Student.Student

sealed class UserInfo {
    data class UserStudent(val studentInfo: Student?) : UserInfo()
    data class UserCompany(val companyInfo: Company?) : UserInfo()
}