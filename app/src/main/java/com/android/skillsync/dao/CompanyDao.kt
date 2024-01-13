package com.android.skillsync.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.skillsync.models.Comapny.Company

@Dao
interface CompanyDao {
    @Query("SELECT * FROM company")
    fun getAllCompanies(): LiveData<List<Company>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(company: Company)

    @Delete
    suspend fun delete(company: Company)

    @Query("DELETE FROM company")
    suspend fun deleteAllCompanies()

    @Update
    suspend fun update(company: Company)

    @Query("SELECT name FROM Company WHERE id =:id")
    suspend fun getCompanyName(id: String): String

    @Query("SELECT logo FROM Company WHERE id =:id")
    suspend fun getCompanyUrlLogo(id: String): String

    @Query("SELECT emailAddress FROM Company WHERE id =:id")
    suspend fun getCompanyAddress(id: String): String
}
