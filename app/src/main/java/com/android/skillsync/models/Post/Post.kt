package com.android.skillsync.models.Post

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "Post")
data class Post(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: UUID,

    @ColumnInfo(name = "userId")
    val userId: Long, // Foreign key referencing User - Group / Company

    @ColumnInfo("Title")
    val title: String,

    @ColumnInfo("Content")
    val content: String,

    @ColumnInfo("Image")
    val imagePath: String
)
