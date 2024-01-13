package com.android.skillsync.models.Group

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

@Entity(tableName = "Group")
data class Group(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "institution")
    val institution: String,

    @ColumnInfo(name = "teamDescription")
    val teamDescription: String,

    @ColumnInfo(name = "memberNames")
    val memberNames: List<String> = emptyList()
)

@TypeConverters(ListStringConverter::class)
class ListStringConverter {
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
}
