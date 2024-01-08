package com.android.skillsync.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Company(val name: String, val location: CompanyLocation)
