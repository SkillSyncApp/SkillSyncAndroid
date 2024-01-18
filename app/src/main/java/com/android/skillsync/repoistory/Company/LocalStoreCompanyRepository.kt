package com.android.skillsync.repoistory.Company

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android.skillsync.dao.AppLocalDatabase
import com.android.skillsync.models.Comapny.Company
import java.util.Date

class LocalStoreCompanyRepository {
    val appLocalDB = AppLocalDatabase
    val companyDao = appLocalDB.db.getCompanyDao()
    val companies: LiveData<List<Company>> = companyDao.getAllCompanies()


    var localTimestamp: Date = Date(0)

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
    suspend fun getAllCompanies() {
        val currentTimestamp = Date()
        updateLocalTimestamp(currentTimestamp)
        companyDao.getAllCompanies()
    }

    suspend fun getLocalTimestamp(): Date {
        return localTimestamp
    }

    suspend fun updateLocalTimestamp(newTimestamp: Date) {
        localTimestamp = newTimestamp
    }
}
