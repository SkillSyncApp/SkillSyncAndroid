package com.android.skillsync.models

import android.provider.ContactsContract
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "Company",
    foreignKeys = [
        ForeignKey(
            entity = CompanyLocation::class,
            parentColumns = ["id"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Company(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("logo")
    val logo: String,

    @ColumnInfo("emailAddress")
    val emailAddress: String,

    @ColumnInfo(name = "locationId")
    val locationId: Long
)
