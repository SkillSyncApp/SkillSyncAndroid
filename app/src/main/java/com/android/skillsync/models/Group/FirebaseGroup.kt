package com.android.skillsync.models.Group

data class FirebaseGroup(
    val name: String,
    val email: String,
    val institution: String,
    val teamDescription: String,
    val memberNames: List<String>,
//    val lastUpdated: Long
)
