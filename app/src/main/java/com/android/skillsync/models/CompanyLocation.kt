package com.android.skillsync.models

import com.firebase.geofire.core.GeoHash
import com.google.firebase.firestore.GeoPoint

data class CompanyLocation(val address: String, val location: GeoPoint, val geoHash: GeoHash)
