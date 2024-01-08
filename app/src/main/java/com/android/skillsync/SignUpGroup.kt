package com.android.skillsync

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.skillsync.databinding.ActivitySignUpGroupBinding
import com.android.skillsync.models.Group
import com.android.skillsync.models.Type
import com.android.skillsync.models.UserType
import com.android.skillsync.services.PlacesApiCall
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore

class SignUpGroup : AppCompatActivity() {

    private lateinit var binding:ActivitySignUpGroupBinding
    private lateinit var firebaseAuth: FirebaseAuth
  //  private lateinit var locationsAdapter: ArrayAdapter<String>

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpGroupBinding.inflate(layoutInflater)

        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()

        binding.signUpBtn.setOnClickListener {
            val groupName = binding.teamName.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val institution = binding.institution.text.toString()
            val teamDescription = binding.teamDescription.text.toString()
            val teamMembers = binding.membersName.text.toString()

            if((groupName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()))
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                if(it.isSuccessful){
                    val groupId = it.result.user?.uid!!
                    createGroup(groupId, groupName,institution,teamDescription,teamMembers)

                }else{
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // remember user
    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser

        if(user != null) {
            val intent = Intent(this,NewPostActivity::class.java)
            intent.putExtra("userEmail", user.email)
            startActivity(intent)
            finish()
        }
    }


    fun createGroup(groupId:String, groupName: String, institution: String, teamDescription: String,teamMembers:String){
        val database = Firebase.firestore
        val intent = Intent(this, SignIn::class.java)

        val groupEntity = Group(groupName,institution,teamDescription,teamMembers)

        val userType = UserType(Type.GROUP)
        database.collection("usersType").document(groupId).set(userType).addOnSuccessListener {
            val groupRef = database.collection("groups").document(groupId)
            groupRef.set(groupEntity).addOnSuccessListener {
                groupRef.addSnapshotListener { snapshot, e ->
                    logGroupNameFromDB(snapshot, e)
                    startActivity(intent)
                }
            }.addOnFailureListener {
                Log.d("ERROR", "fail to create group to app")
            }
        }
    }

    // TODO remove
    fun logGroupNameFromDB(snapshot: DocumentSnapshot?, e:  FirebaseFirestoreException?) {
        if (e != null) Log.d("ERROR", "Listen failed.", e)

        if (snapshot != null && snapshot.exists()) {
            val groupName = snapshot.getString("name")
            Log.d("SUCCESS", "Group Name: $groupName")
        } else {
            Log.d("ERROR", "Group snapshot is null or does not exist.")
        }
    }

//    @RequiresApi(Build.VERSION_CODES.O_MR1)
//    fun searchPlaces(query: String) {
//        PlacesApiCall().getPlacesByQuery(this, query) { places ->
//            locationsAdapter.clear()
//            places?.forEach { locationsAdapter.add(it.title.plus(" | ").plus(it.address)) }
//            locationsAdapter.notifyDataSetChanged()
//        }
//    }
}
