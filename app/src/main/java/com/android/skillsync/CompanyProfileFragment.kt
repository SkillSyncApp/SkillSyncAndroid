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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.ViewModel.CompanyViewModel
import com.android.skillsync.ViewModel.PostViewModel
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.android.skillsync.adapters.PostAdapter
import com.squareup.picasso.Picasso

class CompanyProfileFragment : Fragment() {

    private val userAuthViewModel: UserAuthViewModel by activityViewModels()
    private lateinit var companyViewModel: CompanyViewModel
    private lateinit var postViewModel: PostViewModel

    private lateinit var view: View

    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter

    private var profileImage: ImageView? = null
    private var profileImageBackgroundElement: ImageView? = null
    private var companyName: TextView? = null
    private var companyBio: TextView? = null
    private var backButton: ImageView? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        companyViewModel = CompanyViewModel()
        postViewModel = PostViewModel()

        view = inflater.inflate(R.layout.fragment_company_profile, container, false)

        postAdapter = PostAdapter(mutableListOf())

        val args = arguments
        val companyId = args?.getString("userId") ?: userAuthViewModel.getUserId()

        if(args?.getString("userId")?.isNotEmpty() == true) {
            backButton = view.findViewById(R.id.back_button)
            backButton?.setVisibility(View.VISIBLE)

            backButton?.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        fillProfileDetails(companyId.toString())
        fillCompanyPosts(companyId.toString())

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

    private fun fillCompanyPosts(companyId: String): Unit {
        postViewModel.getPostsByOwnerId(companyId) {
            postsRecyclerView = view.findViewById(R.id.companyPostsRecyclerView)
            postsRecyclerView.setPadding(0, 0, 0, 250)
            postAdapter.posts = it.toMutableList()
            postsRecyclerView.layoutManager = LinearLayoutManager(context)
            postsRecyclerView.adapter = postAdapter
        }
    }
}