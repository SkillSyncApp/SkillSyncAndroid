package com.android.skillsync.models.Post

data class FirebasePost(
    val userId: Long,
    val title: String,
    val content: String,
    val imagePath: String,
    val lastUpdated: Long
)
