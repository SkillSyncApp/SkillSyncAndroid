package com.android.skillsync.models.Group

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Group")
data class Group(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: UUID,

    @ColumnInfo("email")
     val email: String,

    @ColumnInfo("name")
     val name: String,

    @ColumnInfo("institution")
     val institution: String,

    @ColumnInfo("details")
     val details: String,

    @ColumnInfo("memberNames")
     val memberNames: List<String>,
)
//data class Group(val name:String, val institution:String?,val teamDescription:String?,val teamMembers:String?);
