package com.android.skillsync.repoistory.Company

import android.util.Log
import com.android.skillsync.models.Comapny.Company
import com.android.skillsync.models.CompanyLocation
import com.android.skillsync.models.Student.Student
import com.android.skillsync.models.UserType
import com.android.skillsync.repoistory.ApiManager
import com.firebase.geofire.core.GeoHash
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.tasks.await
import com.android.skillsync.models.Type
import com.android.skillsync.repoistory.Auth.FireStoreAuthRepository.Companion.USER_TYPE_COLLECTION_PATH
import com.android.skillsync.repoistory.Student.FireStoreStudentRepository

class FireStoreCompanyRepository {
    companion object {
        const val COMPANIES_COLLECTION_PATH = "companies"
    }

    val apiManager = ApiManager()

    fun getCompany(companyId: String, callback: (company: Company) -> Unit) {
        val companyDocument = apiManager.db.collection(COMPANIES_COLLECTION_PATH)

        companyDocument.whereEqualTo("id", companyId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.isEmpty) {
                    val data = documentSnapshot.documents[0].data;

                    if (data != null) {
                        if (data.isNotEmpty()) {
                            val timestamp: Timestamp? = data["lastUpdated"] as? Timestamp
                            var lastUpdated: Long? = null
                            timestamp?.let {
                                lastUpdated = it.seconds
                            }

                            val locationData = data["location"] as? Map<*, *>
                            if (locationData != null) {
                                val address = locationData["address"] as String
                                val geoPoint = locationData["location"] as GeoPoint
                                val latitude = geoPoint.latitude
                                val longitude = geoPoint.longitude

                                val companyLocation = CompanyLocation(
                                    address,
                                    geoPoint,
                                    GeoHash(latitude, longitude)
                                )

                                val company = Company(
                                    name = data["name"] as String,
                                    email = data["email"] as String,
                                    bio = data["bio"] as String,
                                    location = companyLocation,
                                    logo = data["logo"] as String,
                                    lastUpdated = lastUpdated ?: 0
                                )

                                callback(company)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { locationException ->
                Log.e("Firestore", "Error getting company: $locationException")
            }
    }

    fun getCompanies(since: Long, callback: (List<Company>) -> Unit) {
        apiManager.db.collection(COMPANIES_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo(Company.LAST_UPDATED, Timestamp(since, 0))
            .get().addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val companies: MutableList<Company> = mutableListOf()
                        for (json in it.result) {
                            val company = Company.fromJSON(json.data)
                            companies.add(company)
                        }
                        callback(companies)
                    }
                    false -> callback(listOf())
                }
            }
    }

    suspend fun addCompany(company: Company, onSuccess: (String) -> Unit): String {
        val documentReference = apiManager.db.collection(COMPANIES_COLLECTION_PATH)
            .add(company.json)
            .await()

        onSuccess(company.id)
        return documentReference.id
    }

    fun setCompanyInUserTypeDB(documentReferenceId: String) = run {
        val userType = UserType(Type.COMPANY)
        apiManager.db.collection(USER_TYPE_COLLECTION_PATH).document(documentReferenceId).set(userType)
    }

    fun setCompaniesOnMap(callback: (Company) -> Unit) {
        val companiesReference = apiManager.db.collection(COMPANIES_COLLECTION_PATH)
        companiesReference.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val companyId = document.id
                    fetchCompanyLocation(companyId, callback)
                }
            }
    }

    private fun fetchCompanyLocation(companyId: String, callback: (Company) -> Unit) {
        val companyDocument = apiManager.db.collection(COMPANIES_COLLECTION_PATH)
        companyDocument.document(companyId).get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.data
                if (data != null) {
                    val locationData = data["location"] as? Map<*, *>
                    if (locationData != null) {
                        val companyLocation = buildCompanyLocation(locationData)

                        val timestamp: Timestamp? = data["lastUpdated"] as? Timestamp
                        var lastUpdated: Long? = null
                        timestamp?.let {
                            lastUpdated = it.seconds
                        }

                        val company = Company(
                            name = data["name"] as String,
                            email = data["email"] as String,
                            logo = data["logo"] as String,
                            bio = data["bio"] as String,
                            location = companyLocation,
                            lastUpdated = lastUpdated ?: 0 // Default to 0 if timestamp is null
                        )
                        callback(company)
                    }
                }
            }
            .addOnFailureListener { locationException ->
                Log.e("Firestore", "Error getting location documents: $locationException")
            }
    }

    private fun buildCompanyLocation(locationData: Map<*, *>?): CompanyLocation {
        val address = locationData?.get("address") as String
        val geoPoint = locationData["location"] as GeoPoint
        val latitude = geoPoint.latitude
        val longitude = geoPoint.longitude

        return CompanyLocation(
            address,
            geoPoint,
            GeoHash(latitude, longitude)
        )
    }

    fun updateCompany(company: Company, data: Map<String, Any>, onSuccessCallBack: () -> Unit, onFailureCallBack: () -> Unit){
        apiManager.db.collection(COMPANIES_COLLECTION_PATH).whereEqualTo("id", company.id).get().addOnSuccessListener {
            apiManager.db.collection(COMPANIES_COLLECTION_PATH).document(it.documents[0].id).update(data)
                .addOnSuccessListener { onSuccessCallBack() }
                .addOnFailureListener { onFailureCallBack() }
        }
    }

}
