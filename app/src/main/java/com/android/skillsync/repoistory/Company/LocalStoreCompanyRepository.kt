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


    var localTimestampProperty: Date = Date(0)

    @WorkerThread
    fun insert(company: Company) {
        companyDao.insert(company)
    }

    @WorkerThread
    fun update(company: Company) {
        companyDao.update(company)
    }

    @WorkerThread
    fun delete(company: Company) {
        companyDao.delete(company)
    }

    @WorkerThread
    fun getAllCompanies() {
        val currentTimestamp = Date()
        updateLocalTimestamp(currentTimestamp)
        companyDao.getAllCompanies()
    }

    fun getLocalTimestamp(): Date {
        return localTimestampProperty
    }

    fun updateLocalTimestamp(newTimestamp: Date) {
        localTimestampProperty = newTimestamp
    }
}
