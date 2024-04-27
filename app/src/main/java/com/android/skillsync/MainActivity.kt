package com.android.skillsync

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.android.skillsync.helpers.ActionBarHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private var userAuthViewModel: UserAuthViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userAuthViewModel = UserAuthViewModel()

        // TODO REMOVE - JUST TO TEST SIGN IN PAGE
        val firebaseAuth = FirebaseAuth.getInstance()
        // Sign out the current user
        firebaseAuth.signOut()

        ActionBarHelper.showActionBarAndBottomNavigationView(this)

        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.navHostMain) as? NavHostFragment
        navController = navHostFragment?.navController

        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.mainActivityBottomNavigationView)
        navController?.let { NavigationUI.setupWithNavController(bottomNavigationView, it) }
    }

    private var userTypeProfile = "COMPANY"
    fun setProfile(userType: String) {
        userTypeProfile = userType
        invalidateOptionsMenu()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.nav_menu, menu)

        // Check if the menu item exists before modifying its visibility
        val companyMenuItem = menu?.findItem(R.id.companyProfileFragment)
        val groupMenuItem = menu?.findItem(R.id.groupProfileFragment)

        if(userTypeProfile == "USER") {
            companyMenuItem?.isVisible = false
            groupMenuItem?.isVisible = true
        } else {
            companyMenuItem?.isVisible = true
            groupMenuItem?.isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController?.navigateUp()
                true
            }
            else -> navController?.let { NavigationUI.onNavDestinationSelected(item, it) } ?: super.onOptionsItemSelected(item)
        }
    }
}
