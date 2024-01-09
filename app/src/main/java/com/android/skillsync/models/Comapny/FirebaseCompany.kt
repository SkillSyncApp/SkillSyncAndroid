package com.android.skillsync.models.Comapny

import com.android.skillsync.models.CompanyLocation

data class FirebaseCompany(
    val name: String,
    val logo: String,
    val emailAddress: String,
    val location: CompanyLocation
)
