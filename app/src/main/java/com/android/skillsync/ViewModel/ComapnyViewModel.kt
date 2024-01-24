package com.android.skillsync.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.skillsync.domain.CompanyUseCases
import com.android.skillsync.models.Comapny.Company
import com.android.skillsync.models.CompanyLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class CompanyViewModel: ViewModel() {
    var companies: LiveData<MutableList<Company>>? = null

    private val companyUseCases: CompanyUseCases = CompanyUseCases()

    fun createUserAsCompanyOwner(email: String, password: String, onSuccessCallBack: () -> Unit, onFailureCallBack: (String?) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        companyUseCases.fireStoreAuthRepository.createUser(email, password, onSuccessCallBack, onFailureCallBack)
    }

    fun getAllCompanies(): LiveData<MutableList<Company>> {
        return companyUseCases.getAllCompanies()
    }

    fun addCompany(company: Company) = viewModelScope.launch(Dispatchers.IO) {
        companyUseCases.addCompany(company)
    }

    fun deleteCompany(company: Company) = viewModelScope.launch(Dispatchers.IO) {
        companyUseCases.deleteCompany(company)
    }

    fun update(company: Company) = viewModelScope.launch(Dispatchers.IO) {
        companyUseCases.update(company)
    }

    fun refreshCompanies() = viewModelScope.launch(Dispatchers.IO) {
        companyUseCases.refreshCompanies()
    }

    fun setCompaniesOnMap(callback: (CompanyLocation) -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        companyUseCases.setCompaniesOnMap(callback)
    }
}
