package com.android.skillsync.Converters

import androidx.room.TypeConverter
import com.android.skillsync.models.CompanyLocation
import com.google.firebase.Timestamp
import java.util.Date
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Timestamp?): Long? {
        return value?.toDate()?.time
    }

    @TypeConverter
    fun toTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(Date(it)) }
    }
     @TypeConverter
    fun fromString(value: String?): List<String>? {
        if (value == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromCompanyLocation(location: CompanyLocation?): String? {
        return location?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toCompanyLocation(locationJson: String?): CompanyLocation? {
        return locationJson?.let { Gson().fromJson(it, CompanyLocation::class.java) }
    }
}
