package com.android.skillsync.repoistory

import com.android.skillsync.dao.CompanyDao

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android.skillsync.models.Comapny.Company

class CompanyRepository (private val companyDao: CompanyDao) {
    val companies: LiveData<List<Company>> = companyDao.getAllCompanies()

    @WorkerThread
    suspend fun insert(company: Company) {
        companyDao.insert(company)
    }

    @WorkerThread
    suspend fun update(company: Company) {
        companyDao.update(company)
    }

    @WorkerThread
    suspend fun delete(company: Company) {
        companyDao.delete(company)
    }

    @WorkerThread
    suspend fun deleteAllCompanies() {
        companyDao.deleteAllCompanies()
    }

    @WorkerThread
    suspend fun getAllCompanies() {
        companyDao.getAllCompanies()
    }
}
