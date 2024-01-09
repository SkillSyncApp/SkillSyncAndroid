package com.android.skillsync.models.User

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.android.skillsync.models.Type
import java.util.UUID

data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("Id")
    val id: UUID,

    @ColumnInfo("Type")
    val type: Type
)
