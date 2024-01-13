package com.android.skillsync.application

import android.app.Application
import com.android.skillsync.dao.AppLocalDatabase
import com.android.skillsync.repoistory.CompanyRepository
import com.android.skillsync.repoistory.PostRepository
import com.android.skillsync.repoistory.GroupRepository


class SkilSyncApplication: Application() {
    val database by lazy { AppLocalDatabase.getInstance(this) }
    val postRepository by lazy { PostRepository(database.getPostDao()) }
    val companyRepository by lazy { CompanyRepository(database.getCompanyDao()) }
    val GroupRepository by lazy { GroupRepository(database.getGroupDao()) }
}
