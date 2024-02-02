package com.android.skillsync.repoistory.Company

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.android.skillsync.dao.AppLocalDatabase
import com.android.skillsync.models.Comapny.Company

class LocalStoreCompanyRepository {
    val appLocalDB = AppLocalDatabase
    val companyDao = appLocalDB.db.getCompanyDao()

    @WorkerThread
    fun add(company: Company) {
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
    fun getAllCompanies(): LiveData<MutableList<Company>> {
        return companyDao.getAllCompanies()
    }
}
