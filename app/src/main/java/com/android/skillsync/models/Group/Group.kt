package com.android.skillsync.models.Group

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Group")
data class Group(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("email")
     val email: String,

    @ColumnInfo("name")
     val name: String,

    @ColumnInfo("institution")
     val institution: String,

    @ColumnInfo("teamDescription")
     val teamDescription: String,

    @ColumnInfo("memberNames")
     val memberNames: List<String>,
)
