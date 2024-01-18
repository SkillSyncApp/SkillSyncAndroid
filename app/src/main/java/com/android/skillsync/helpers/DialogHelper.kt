package com.android.skillsync.helpers
import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogHelper(private val context: Context, private val error: String?) {

    fun showErrorDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton("OK") { dialog, _ ->
                // Handle positive button click if needed
                dialog.dismiss()
            }
            .show()
    }
}
