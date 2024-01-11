package com.android.skillsync.models

import com.google.firebase.firestore.GeoPoint

data class CompanyLocation(val address: String, val location: GeoPoint)
