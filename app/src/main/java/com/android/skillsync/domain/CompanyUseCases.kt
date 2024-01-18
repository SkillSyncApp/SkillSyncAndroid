package com.android.skillsync.domain

import androidx.lifecycle.LiveData
import com.android.skillsync.models.Comapny.Company
import com.android.skillsync.repoistory.Auth.FireStoreAuthRepository
import com.android.skillsync.repoistory.Company.FireStoreCompanyRepository
import com.android.skillsync.repoistory.Company.LocalStoreCompanyRepository

class CompanyUseCases {
    private val localStoreCompanyRepository: LocalStoreCompanyRepository = LocalStoreCompanyRepository()
    private val fireStoreCompanyRepository: FireStoreCompanyRepository = FireStoreCompanyRepository()
    val fireStoreAuthRepository: FireStoreAuthRepository = FireStoreAuthRepository()

    val companiesLiveData: LiveData<List<Company>> get() = localStoreCompanyRepository.companies

    fun getAllCompanies() {
        val localTimestamp = localStoreCompanyRepository.getLocalTimestamp()
        // fetch the new companies
        fireStoreCompanyRepository.getNewCompanies(localTimestamp)
    }

    suspend fun addCompany(company: Company) {
        val companyId = fireStoreCompanyRepository.addCompany(company)
        company.id = companyId
        fireStoreCompanyRepository.addCompany(company)
    }

    suspend fun deleteCompany(company: Company) {
        localStoreCompanyRepository.delete(company)
    }

    suspend fun update(company: Company) {
        localStoreCompanyRepository.update(company)
    }
}
