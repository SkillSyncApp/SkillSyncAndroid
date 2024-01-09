//package com.android.skillsync.dao
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Update
//import com.android.skillsync.models.ComapnyModel.Company
//import androidx.room.Query
//
//@Dao
//interface CompanyDao {
//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    abstract suspend fun addCompany(company: Company)
//
//    @Delete
//    abstract suspend fun deleteCompany(company: Company)
//
//    @Update
//    abstract fun updateCompanyName(company: Company)
//
//    @Update
//    abstract fun updateCompanyLogo(company: Company)
//
//    @Query("SELECT companyAddress FROM `Company location` WHERE companyId =:id")
//    abstract fun getCompanyAddress(id: String)
//}
