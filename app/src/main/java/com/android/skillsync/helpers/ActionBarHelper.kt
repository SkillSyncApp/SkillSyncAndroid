package com.android.skillsync.helpers

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.skillsync.R
import com.google.android.material.bottomnavigation.BottomNavigationView

object ActionBarHelper {

    fun hideActionBarAndBottomNavigationView(activity: AppCompatActivity?) {
        if (activity != null) {
            activity.supportActionBar?.hide()

            val bottomNavigationView: BottomNavigationView? =
                activity.findViewById(R.id.mainActivityBottomNavigationView)
            bottomNavigationView?.visibility = View.GONE
        }
    }

    fun showActionBarAndBottomNavigationView(activity: AppCompatActivity?) {
        if (activity != null) {
            activity.supportActionBar?.show()

            val bottomNavigationView: BottomNavigationView? =
                activity.findViewById(R.id.mainActivityBottomNavigationView)
            bottomNavigationView?.visibility = View.VISIBLE
        }
    }
}
