package com.android.skillsync.models

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

     @ColumnInfo("details")
     val details: String,

     @ColumnInfo("memberNames")
     val memberNames: List<String>,
)
//data class Group(val name:String, val institution:String?,val teamDescription:String?,val teamMembers:String?);
