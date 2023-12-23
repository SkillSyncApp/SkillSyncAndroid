package com.android.skillsync.models

enum class Type {
    GROUP,
    COMPANY
}
data class UserType(val type: Type);
