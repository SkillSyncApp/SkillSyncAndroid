package com.android.skillsync.helpers

import android.content.Intent
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso

class ImageHelper(private val fragment: Fragment, private val imageView: ImageView) {

    private var imageUrl: String? = null

    private val startForResult =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data
                selectedImageUri?.let {
                    imageUrl = it.toString()
                    Picasso.get().load(it).into(imageView)
                }
            }
        }

    fun uploadImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startForResult.launch(intent)
    }

    fun getImageUrl(): String? {
        return imageUrl
    }

    fun setImageViewClickListener() {
        imageView.setOnClickListener {
            uploadImage()
        }
    }
}
