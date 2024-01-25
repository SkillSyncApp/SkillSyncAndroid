package com.android.skillsync.models.Student

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.skillsync.base.MyApplication
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
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
    var lastUpdated: Long? = null

) {
    companion object {
        var lastUpdated: Long
            get() {
                return MyApplication.Globals
                    .appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    ?.getLong(GET_LAST_UPDATED, 0) ?: 0
            }
            set(value) {
                MyApplication.Globals
                    ?.appContext
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.edit()
                    ?.putLong(GET_LAST_UPDATED, value)?.apply()
            }

        const val NAME_KEY = "name"
        const val EMAIL_KEY = "email"
        const val INSTITUTION_KEY = "institution"
        const val IMAGE_KEY = "image"
        const val BIO_KEY = "bio"
        const val LAST_UPDATED = "lastUpdated"
        const val GET_LAST_UPDATED = "get_last_updated"

        fun fromJSON(json: Map<String, Any>): Student{
            val name = json[NAME_KEY] as? String ?: ""
            val email = json[EMAIL_KEY] as? String ?: ""
            val institution = json[INSTITUTION_KEY] as? String ?: ""
            val bio = json[BIO_KEY] as? String ?: ""
            val image = json[IMAGE_KEY] as? String ?: ""

            val student = Student(
                name = name,
                email = email,
                institution = institution,
                image = image,
                bio = bio
            )

            val timestamp: Timestamp? = json[LAST_UPDATED] as? Timestamp
            timestamp?.let {
                student.lastUpdated = it.seconds
            }

            return student
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                EMAIL_KEY to email,
                INSTITUTION_KEY to institution,
                IMAGE_KEY to image,
                BIO_KEY to bio,
                LAST_UPDATED to FieldValue.serverTimestamp()
            )
        }
}
