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
import com.android.skillsync.ViewModel.CompanyViewModel
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.squareup.picasso.Picasso

class CompanyProfileFragment : Fragment() {

    private val userAuthViewModel: UserAuthViewModel by activityViewModels()
    private lateinit var companyViewModel: CompanyViewModel
    private lateinit var view: View

    private var profileImage: ImageView? = null
    private var profileImageBackgroundElement: ImageView? = null
    private var companyName: TextView? = null
    private var companyBio: TextView? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        companyViewModel = CompanyViewModel()
        view = inflater.inflate(R.layout.fragment_company_profile, container, false)

        // TODO: use userId from args if got one, use current user if not
        val userId = userAuthViewModel.getUserId()
        fillProfileDetails(userId.toString())

        return view
    }

    private fun fillProfileDetails(companyId: String): Unit {
        companyViewModel.getCompany(companyId) {
            companyName = view.findViewById(R.id.companyName)
            companyName?.text = it.name

            companyBio = view.findViewById(R.id.companyBio)
            companyBio?.text = it.bio

            profileImage = view.findViewById(R.id.companyImage)
            profileImageBackgroundElement = view.findViewById(R.id.company_profile_background_image);
            if (profileImage != null && profileImageBackgroundElement != null) {
                Picasso.get().load(it.logo).into(profileImage)
                Picasso.get().load(it.logo).into(profileImageBackgroundElement)

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