package com.android.skillsync.domain

import android.util.Log
import androidx.lifecycle.LiveData
import com.android.skillsync.models.Comapny.Company
import com.android.skillsync.models.CompanyLocation
import com.android.skillsync.repoistory.Auth.FireStoreAuthRepository
import com.android.skillsync.repoistory.Company.FireStoreCompanyRepository
import com.android.skillsync.repoistory.Company.LocalStoreCompanyRepository
import java.util.concurrent.Executors

class CompanyUseCases {
    private val localStoreCompanyRepository: LocalStoreCompanyRepository = LocalStoreCompanyRepository()
    private val fireStoreCompanyRepository: FireStoreCompanyRepository = FireStoreCompanyRepository()
    val fireStoreAuthRepository: FireStoreAuthRepository = FireStoreAuthRepository()

    private var executor = Executors.newSingleThreadExecutor()
    val companies: LiveData<MutableList<Company>>? = null

    fun getAllCompanies(): LiveData<MutableList<Company>> {
        refreshCompanies()
        return companies ?: localStoreCompanyRepository.getAllCompanies()
    }

    fun refreshCompanies() {

        // 1. Get last local update
        val lastUpdated: Long = Company.lastUpdated

        // 2. Get all updated records from firestore since last update locally
        fireStoreCompanyRepository.getCompanies(lastUpdated) { list ->
            Log.i("TAG", "Firebase returned ${list.size}, lastUpdated: $lastUpdated")
            // 3. Insert new record to ROOM
            executor.execute {
                var time = lastUpdated
                for (company in list) {
                    localStoreCompanyRepository.insert(company)

                    company.lastUpdated?.let {
                        if (time < it)
                            time = company.lastUpdated ?: System.currentTimeMillis()
                    }
                }

                // 4. Update local data
                Company.lastUpdated = time
            }
        }
    }

    suspend fun addCompany(company: Company) {
        val companyId = fireStoreCompanyRepository.addCompany(company)
        company.id = companyId
    }

    suspend fun deleteCompany(company: Company) {
//        fireStoreCompanyRepository.delete(company)
    }

    suspend fun update(company: Company) {
//        fireStoreCompanyRepository.(company)
    }

    fun setCompaniesOnMap(callback: (MutableList<Company>) -> Unit) {
        fireStoreCompanyRepository.setCompaniesOnMap(callback)
    }
}
