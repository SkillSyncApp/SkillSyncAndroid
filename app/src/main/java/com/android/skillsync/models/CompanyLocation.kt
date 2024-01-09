package com.android.skillsync.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Company location")
data class CompanyLocation(
    @PrimaryKey
    @ColumnInfo(name = "companyId")
    val companyId: Long,

    @ColumnInfo("companyAddress")
    val address: String,

    @ColumnInfo("longitude")
    val longitude: Float,

    @ColumnInfo("latitude")
    val latitude: Float)
