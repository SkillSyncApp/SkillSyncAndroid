package com.android.skillsync.models.Post

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.android.skillsync.models.Student.Student
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.UUID

@Entity(
    tableName = "Post",
    foreignKeys = [ForeignKey(
        entity = Student::class,
        parentColumns = ["id"],
        childColumns = ["ownerId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Post(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(), // temp id
    val ownerId: String, // Foreign key referencing owner - User / Company
    val title: String,
    val content: String,
    val imagePath: String,
    @ServerTimestamp
    val createdDate: Timestamp = Timestamp.now(),
    @ServerTimestamp
    val updatedDate: Timestamp = Timestamp.now(),
) {
    companion object {
        const val OWNER_ID_KEY = "ownerId"
        const val TITLE_KEY = "title"
        const val CONTENT_KEY = "content"
        const val IMAGE_PATH_KEY = "imagePath"
        const val CREATED_DATE_KEY = "createdDate"
        const val UPDATED_DATE_KEY = "updatedDate"

        fun fromJSON(json: Map<String, Any>): Post {
            val ownerId = json[OWNER_ID_KEY] as? String ?: "" // "" as unknown
            val title = json[TITLE_KEY] as? String ?: ""
            val content = json[CONTENT_KEY] as? String ?: ""
            val imagePath = json[IMAGE_PATH_KEY] as? String ?: ""
            val createdDate = json[CREATED_DATE_KEY] as? Timestamp ?: Timestamp.now()
            val updatedDate = json[UPDATED_DATE_KEY] as? Timestamp ?: Timestamp.now()

            return Post(
                ownerId = ownerId,
                title = title,
                content = content,
                imagePath = imagePath,
                createdDate = createdDate,
                updatedDate = updatedDate
            )
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                OWNER_ID_KEY to ownerId,
                TITLE_KEY to title,
                CONTENT_KEY to content,
                IMAGE_PATH_KEY to imagePath,
                CREATED_DATE_KEY to createdDate,
                UPDATED_DATE_KEY to updatedDate
            )
        }
}
