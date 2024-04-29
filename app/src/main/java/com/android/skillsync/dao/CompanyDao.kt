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
    fun getAllCompanies(): LiveData<MutableList<Company>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(company: Company)

    @Delete
    fun delete(company: Company)

    @Query("DELETE FROM company")
    fun deleteAllCompanies()

    @Update
    fun update(company: Company)
}
