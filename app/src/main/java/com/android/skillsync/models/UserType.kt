package com.android.skillsync.models
enum class Type {
    STUDENT,
    COMPANY
}
data class UserType(val type: Type);