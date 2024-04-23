package com.android.skillsync

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.skillsync.ViewModel.StudentViewModel
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.squareup.picasso.Picasso

class GroupProfileFragment : Fragment() {

    private val userAuthViewModel: UserAuthViewModel by activityViewModels()
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var view: View

    private var profileImage: ImageView? = null
    private var profileImageBackgroundElement: ImageView? = null
    private var groupName: TextView? = null
    private var groupBio: TextView? = null
    private var groupInstitution: TextView? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        studentViewModel = StudentViewModel()
        view = inflater.inflate(R.layout.fragment_group_profile, container, false)

        // TODO: use userId from args if got one, use current user if not
        val userId = userAuthViewModel.getUserId().toString()

        fillProfileDetails(userId)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = userAuthViewModel.getUserId().toString()
        userAuthViewModel.setMenuByUserType(userId, this)
    }

    private fun fillProfileDetails(studentId: String): Unit {
        studentViewModel.getStudent(studentId) {
            groupName = view.findViewById(R.id.groupName)
            groupName?.text = it.name

            groupBio = view.findViewById(R.id.groupBio)
            groupBio?.text = it.bio

            groupInstitution = view.findViewById(R.id.groupCollage);
            groupInstitution?.text = it.institution

            profileImage = view.findViewById(R.id.groupImage)
            profileImageBackgroundElement = view.findViewById(R.id.group_profile_background_image);
            if (profileImage != null && profileImageBackgroundElement != null) {
                Picasso.get().load(it.image).into(profileImage)
                Picasso.get().load(it.image).into(profileImageBackgroundElement)

                // Blur the background image
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    profileImageBackgroundElement?.setRenderEffect(
                        RenderEffect.createBlurEffect(
                            50f, 50f, Shader.TileMode.CLAMP
                        )
                    )
                }

            }
        }
    }

}