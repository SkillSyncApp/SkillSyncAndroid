package com.android.skillsync.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("Id")
    val id: Long,

    @ColumnInfo("Type")
    val type: Type)
