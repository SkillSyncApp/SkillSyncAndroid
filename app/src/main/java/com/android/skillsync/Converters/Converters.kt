package com.android.skillsync.Converters

import androidx.room.TypeConverter
import com.android.skillsync.models.CompanyLocation
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromCompanyLocation(location: CompanyLocation?): String? {
        return location?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toCompanyLocation(locationJson: String?): CompanyLocation? {
        return locationJson?.let { Gson().fromJson(it, CompanyLocation::class.java) }
    }
}
