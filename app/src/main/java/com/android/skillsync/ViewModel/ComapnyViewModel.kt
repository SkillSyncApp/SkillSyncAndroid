package com.android.skillsync.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.skillsync.models.Comapny.Company
import com.android.skillsync.repoistory.CompanyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class CompanyViewModel(private val companyRepository: CompanyRepository): ViewModel() {

    // LiveData to observe changes in the list of posts - TODO check why we need it?
    private val _companiesLiveData = companyRepository.companies
    val companyLiveData: LiveData<List<Company>> get() = _companiesLiveData

    fun getAllCompanies() = viewModelScope.launch(Dispatchers.IO) {
        companyRepository.getAllCompanies()
    }

    fun addCompany(company: Company) = viewModelScope.launch(Dispatchers.IO) {
        companyRepository.insert(company)
    }

    fun deleteCompany(company: Company) = viewModelScope.launch(Dispatchers.IO) {
        companyRepository.delete(company)
    }

    fun deleteAllCompanies() = viewModelScope.launch(Dispatchers.IO) {
        companyRepository.deleteAllCompanies()
    }

    fun update(company: Company) = viewModelScope.launch(Dispatchers.IO) {
        companyRepository.update(company)
    }
}

class CompanyViewModelFactory(private val repository: CompanyRepository): ViewModelProvider.Factory
