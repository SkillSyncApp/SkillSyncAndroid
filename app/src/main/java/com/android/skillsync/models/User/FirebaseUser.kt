package com.android.skillsync.models.User

import com.android.skillsync.models.Type
import java.util.UUID

data class FirebaseUser(
    val id: UUID,
    val type: Type
)
