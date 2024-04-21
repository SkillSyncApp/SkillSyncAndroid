package com.android.skillsync.domain

import android.util.Log
import androidx.lifecycle.LiveData
import com.android.skillsync.models.Comapny.Company
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

    fun getCompany(companyId: String, callback: (company: Company) -> Unit) {
        fireStoreCompanyRepository.getCompany(companyId, callback);
    }

    fun refreshCompanies() {

        // 1. Get last local update
        val lastUpdated: Long = Company.lastUpdated

        // 2. Get all updated records from firestore since last update locally
        fireStoreCompanyRepository.getCompanies(lastUpdated) { companies ->
            Log.i("TAG", "Firebase returned ${companies.size}, lastUpdated: $lastUpdated")
            // 3. Insert new record to ROOM
            executor.execute {
                var time = lastUpdated
                for (company in companies) {
                    localStoreCompanyRepository.add(company)

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

    suspend fun add(company: Company) {
       fireStoreCompanyRepository.addCompany(company) { companyId ->
           fireStoreCompanyRepository.setCompanyInUserTypeDB(companyId)
            refreshCompanies()
        }
    }

    suspend fun update(company: Company) {
//        fireStoreCompanyRepository.(company)
    }

    fun setCompaniesOnMap(callback: (Company) -> Unit) {
        fireStoreCompanyRepository.setCompaniesOnMap(callback)
    }
}
