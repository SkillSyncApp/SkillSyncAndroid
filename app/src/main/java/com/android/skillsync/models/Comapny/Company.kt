package com.android.skillsync.models.Comapny

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Company")
data class Company(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("logo")
    val logo: String, // TO DO Picasso

    @ColumnInfo("emailAddress")
    val emailAddress: String)

// QUESTION maybe we do need to save location - LIVE DATA
