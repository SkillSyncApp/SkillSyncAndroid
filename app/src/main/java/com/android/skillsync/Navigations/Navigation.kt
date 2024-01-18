package com.android.skillsync.Navigations

import android.view.View
import androidx.navigation.findNavController

fun View.navigate(destinationId: Int) {
    val navController = findNavController()
    navController.navigate(destinationId)
}
