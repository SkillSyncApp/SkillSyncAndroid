package com.android.skillsync.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.skillsync.Converters.Converters
import com.android.skillsync.base.MyApplication
import com.android.skillsync.models.Comapny.Company
import com.android.skillsync.models.Post.Post
import com.android.skillsync.models.Student.Student

@TypeConverters(Converters::class)
@Database(entities = [Post::class, Company::class, Student:: class], version = 1)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun getPostDao(): PostDao
    abstract fun getCompanyDao(): CompanyDao

    abstract fun getStudentDao(): StudentDao
}

object AppLocalDatabase {

    val db: AppLocalDbRepository by lazy {

        val context = MyApplication.Globals.appContext
            ?: throw IllegalStateException("Application context not available")

        Room.databaseBuilder(
            context,
            AppLocalDbRepository::class.java,
            "SkillSyncDatabase.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
