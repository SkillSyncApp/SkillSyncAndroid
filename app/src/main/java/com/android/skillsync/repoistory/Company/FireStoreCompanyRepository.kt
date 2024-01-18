package com.android.skillsync.repoistory.Company

import com.android.skillsync.models.Comapny.Company
import com.android.skillsync.repoistory.ApiManager
import com.google.firebase.Timestamp
import java.util.Date

class FireStoreCompanyRepository {
    companion object {
        const val COMPANIES_COLLECTION_PATH = "companies"
    }

    val apiManager = ApiManager()
    val firebaseAuth = apiManager.firebaseAuth

    fun getNewCompanies(since: Date) {
        val sinceTimestamp = Timestamp(since)

        apiManager.db.collection(COMPANIES_COLLECTION_PATH).whereGreaterThanOrEqualTo("updatedDate", sinceTimestamp).get()
            .addOnSuccessListener {
//                onSuccessCallBack()

            }.addOnFailureListener {
//                onFailureCallBack()
            }
    }

    fun addCompany(company: Company) {
        apiManager.db.collection(COMPANIES_COLLECTION_PATH).document(company.id.toString()).set(company.json)
            .addOnFailureListener {
//                onSuccessCallBack()
            }
            .addOnFailureListener {
//                onFailureCallBack()
            }
    }
}


//
//    fun setCompaniesOnMap(callback: (CompanyLocation) -> Unit) {
//        val companiesReference = firestore.collection(COMPANIES_COLLECTION_PATH)
//
//        companiesReference.get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val companyId = document.id
//                    fetchLocationsCompanies(companyId, callback)
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.e("Firestore", "Error getting company documents: $exception")
//            }
//    }

//    private fun fetchLocationsCompanies(companyId: String, callback: (CompanyLocation) -> Unit) {
//        val locationsCollection = firestore.collection("$COMPANIES_COLLECTION_PATH/$companyId/locations")
//
//        locationsCollection.get()
//            .addOnSuccessListener { locationDocuments ->
//                for (locationDocument in locationDocuments) {
//                    fetchCompanyLocation(companyId, callback)
//                }
//            }
//            .addOnFailureListener { locationException ->
//                Log.e("Firestore", "Error getting location documents: $locationException")
//            }
//    }

//    private fun fetchCompanyLocation(companyId: String, callback: (CompanyLocation) -> Unit) {
//        val companyDocument = firestore.collection(COMPANIES_COLLECTION_PATH).document(companyId)
//
//        companyDocument.get()
//            .addOnSuccessListener { documentSnapshot ->
//                val data = documentSnapshot.data?.get("location") as? Map<*, *>
//                if (data != null) {
//                    val address = data["address"] as? String ?: "Unknown Address"
//                    val geoPoint = data["location"] as? com.google.firebase.firestore.GeoPoint
//                    if (geoPoint != null) {
//                        val latitude = geoPoint.latitude
//                        val longitude = geoPoint.longitude
//
//                        val locationData = CompanyLocation(
//                            address,
//                            com.google.firebase.firestore.GeoPoint(latitude, longitude)
//                        )
//                        callback(locationData)
//                    } else {
//                        Log.e("Firestore", "Error getting location documents: No GeoPoint found")
//                    }
//                } else {
//                    Log.e("Firestore", "Error getting location documents: No data found")
//                }
//            }
//            .addOnFailureListener { locationException ->
//                Log.e("Firestore", "Error getting location documents: $locationException")
//            }
//    }
//}
