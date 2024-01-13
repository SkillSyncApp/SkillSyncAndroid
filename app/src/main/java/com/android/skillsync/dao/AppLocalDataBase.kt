package com.android.skillsync.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.skillsync.models.Comapny.Company
import com.android.skillsync.models.Post.Post

@Database(entities = [Post::class, Company::class], version = 1)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun getPostDao(): PostDao
    abstract fun getCompanyDao(): CompanyDao

    abstract fun getGroupDao(): GroupDao
}

object AppLocalDatabase {

    private var instance: AppLocalDbRepository? = null

    fun getInstance(context: Context): AppLocalDbRepository {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppLocalDbRepository {
            return Room.databaseBuilder(
                context.applicationContext,
                AppLocalDbRepository::class.java,
                "SkillSyncDatabase"
            )
                .build()
        }
}
