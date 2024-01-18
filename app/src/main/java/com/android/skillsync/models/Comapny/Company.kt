package com.android.skillsync.models.Comapny

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.skillsync.models.CompanyLocation
import com.firebase.geofire.core.GeoHash
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import kotlin.random.Random

@Entity
data class Company(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val logo: String,
    val bio: String,
    val location: CompanyLocation,
    @ServerTimestamp
    val createdDate: Timestamp? = null,
    @ServerTimestamp
    val updatedDate: Timestamp? = null
) {

    companion object {
        const val ID_KEY = "id"
        const val NAME_KEY = "name"
        const val EMAIL_KEY = "email"
        const val LOGO_KEY = "logo"
        const val BIO_KEY = "bio"
        const val LOCATION_KEY = "location"
        const val CREATED_DATE_KEY = "createdDateTime"
        const val UPDATED_DATE_KEY = "updatedDateTime"

        fun fromJSON(json: Map<String, Any>): Company {
            val name = json[NAME_KEY] as? String ?: ""
            val email = json[EMAIL_KEY] as? String ?: ""
            val logo = json[LOGO_KEY] as? String ?: ""
            val bio = json[BIO_KEY] as? String ?: ""
            val location = json[LOCATION_KEY] as? CompanyLocation ?: CompanyLocation("Unknown", GeoPoint(0.0,0.0), GeoHash(0.0,0.0) )
            val createdDate = json[CREATED_DATE_KEY] as? Timestamp ?: Timestamp.now()
            val updatedDate = json[UPDATED_DATE_KEY] as? Timestamp ?: Timestamp.now()

            return Company(
                name = name,
                email = email,
                logo = logo,
                location = location,
                bio = bio,
                createdDate = createdDate,
                updatedDate = updatedDate
            )
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                NAME_KEY to name,
                EMAIL_KEY to email,
                LOGO_KEY to logo,
                BIO_KEY to bio,
                LOCATION_KEY to location,
                CREATED_DATE_KEY to (createdDate ?: Timestamp.now()),
                UPDATED_DATE_KEY to (updatedDate ?: Timestamp.now())
            )
        }
}
