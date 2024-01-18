package com.android.skillsync.models.Student

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.UUID

@Entity
data class Student(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(), // temp id
    val name: String,
    val email: String,
    val institution: String,
    val image: String,
    val bio: String,
    @ServerTimestamp
    val createdDate: Timestamp = Timestamp.now(),
    @ServerTimestamp
    val updatedDate: Timestamp = Timestamp.now(),
) {
    companion object {
        private const val NAME_KEY = "name"
        private const val EMAIL_KEY = "email"
        private const val INSTITUTION_KEY = "institution"
        private const val IMAGE_KEY = "image"
        private const val BIO_KEY = "bio"
        private const val CREATED_DATE_KEY = "createdDate"
        private const val UPDATED_DATE_KEY = "updatedDate"

        fun fromJSON(json: Map<String, Any>): Student{
            val name = json[NAME_KEY] as? String ?: ""
            val email = json[EMAIL_KEY] as? String ?: ""
            val institution = json[INSTITUTION_KEY] as? String ?: ""
            val bio = json[BIO_KEY] as? String ?: ""
            val image = json[IMAGE_KEY] as? String ?: ""
            val createdDate = json[CREATED_DATE_KEY] as? Timestamp ?: Timestamp.now()
            val updatedDate = json[UPDATED_DATE_KEY] as? Timestamp ?: Timestamp.now()

            return Student(
                name = name,
                email = email,
                institution = institution,
                image = image,
                bio = bio,
                createdDate = createdDate,
                updatedDate = updatedDate
            )
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                EMAIL_KEY to email,
                INSTITUTION_KEY to institution,
                IMAGE_KEY to image,
                BIO_KEY to bio,
                CREATED_DATE_KEY to createdDate,
                UPDATED_DATE_KEY to updatedDate
            )
        }
}
