package com.android.skillsync.models.Post

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Post")
data class Post(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo(name = "userId")
    val userId: Long, // Foreign key referencing User - Group / Company

    @ColumnInfo("Title")
    val title: String,

    @ColumnInfo("Content")
    val content: String,

    @ColumnInfo("Image")
    val imagePath: String // TO DO Picasso
)
