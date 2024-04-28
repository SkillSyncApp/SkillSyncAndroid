package com.android.skillsync.helpers

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

interface ImageUploadListener {
    fun onImageUploaded(imageUrl: String)
}

class ImageHelper(val fragment: Fragment, private val imageView: ImageView) {

    private var uploadListener: ImageUploadListener? = null
    private var imageUrl: String? = null
    private val storageRef = FirebaseStorage.getInstance().reference

    private val startForResult =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data
                selectedImageUri?.let { uri ->
                    // Upload the image to Firebase Storage
                    uploadImageToFirebaseStorage(uri)
                }
            }
        }

    private fun uploadImageToFirebaseStorage(imageUri: Uri) {
        val randomKey = UUID.randomUUID().toString()
        val riversRef = storageRef.child("images/$randomKey")
        riversRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, get the download URL
                riversRef.downloadUrl.addOnSuccessListener { uri ->
                    // Store the uploaded image URL
                    imageUrl = uri.toString()
                    Log.d("url", imageUrl.toString())

                    // Load the image using Picasso
                    Picasso.get().load(uri).into(imageView)

                    imageUrl?.let { uploadListener?.onImageUploaded(it) }

                }
            }
            .addOnFailureListener { exception ->
                 Log.e("Upload File", "Upload failed", exception)
            }
    }


    fun setImageUploadListener(listener: ImageUploadListener) {
        this.uploadListener = listener
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
