package com.android.skillsync.models.Group

data class FirebaseGroup(
    val email: String,
    val name: String,
    val institution: String,
    val details: String,
    val memberNames: List<String>,
)
//data class Group(val name:String, val institution:String?,val teamDescription:String?,val teamMembers:String?);
