package com.android.skillsync

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.android.skillsync.ViewModel.PostViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    lateinit var postViewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO REMOVE - JUST TO TEST SIGN IN PAGE
        val firebaseAuth = FirebaseAuth.getInstance()
        // Sign out the current user
        firebaseAuth.signOut()

// TODO in posts feed
//        postViewModel = ViewModelProvider(this).get(PostViewModel:: class.java)
//
//        postViewModel.postsLiveData.observe(this, { posts ->
//            // show posts in ui
//        })

        val navHostFragment: NavHostFragment? = supportFragmentManager.findFragmentById(R.id.nav_graph) as? NavHostFragment
        navController = navHostFragment?.navController
        navController?.let { NavigationUI.setupActionBarWithNavController(this, it) }
    }
}
