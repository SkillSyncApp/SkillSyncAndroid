package com.android.skillsync

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.skillsync.helpers.ActionBarHelper

open class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide the ActionBar and BottomNavigationView when the fragment is created
        ActionBarHelper.hideActionBarAndBottomNavigationView(requireActivity() as? AppCompatActivity)
    }
}
