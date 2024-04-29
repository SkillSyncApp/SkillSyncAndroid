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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.ViewModel.CompanyViewModel
import com.android.skillsync.ViewModel.PostViewModel
import com.android.skillsync.ViewModel.StudentViewModel
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.android.skillsync.adapters.PostAdapter
import com.android.skillsync.databinding.FragmentProfileBinding
import com.android.skillsync.helpers.ActionBarHelper
import com.android.skillsync.models.UserInfo
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private val userAuthViewModel: UserAuthViewModel by activityViewModels()

    private lateinit var companyViewModel: CompanyViewModel
    private lateinit var studentViewModel: StudentViewModel

    private lateinit var postViewModel: PostViewModel

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var view: View

    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter

    private var profileImage: ImageView? = null
    private var profileImageBackgroundElement: ImageView? = null
    private var nameTV: TextView? = null
    private var bioTV: TextView? = null
    private var additionalInfoTV: TextView? = null
    private var noPostsWarningTV: TextView? = null

    private var editButton: ImageView? = null
    private var logoutButton: TextView? = null
    private var backButton: ImageView? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        companyViewModel = CompanyViewModel()
        studentViewModel = StudentViewModel()
        postViewModel = PostViewModel()

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        view  = binding.root

        postAdapter = PostAdapter(mutableListOf(), false)

        val args = arguments
        val userId = args?.getString("userId") ?: userAuthViewModel.getUserId()

        if (userId !== null) {
            userAuthViewModel.getInfoOnUser(userId) { userInfo, error ->
                when (userInfo) {
                    is UserInfo.UserStudent -> {
                        getGroup(userId)
                    }
                    is UserInfo.UserCompany -> {
                        getCompany(userId);
                    }

                    null -> TODO()
                }
            }

            handleProfileActionButtons(userId)
            fillPosts(userId)
        }

        if(args?.getString("userId")?.isNotEmpty() == true) {
            backButton = view.findViewById(R.id.back_button)
            backButton?.setVisibility(View.VISIBLE)

            // TODO - check back in real device
            backButton?.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack(
                    "profileBackStack",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            }
        }

        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        ActionBarHelper.showActionBarAndBottomNavigationView(requireActivity() as? AppCompatActivity)
        super.onCreate(savedInstanceState)
    }

    private fun getCompany(companyId: String): Unit {
        companyViewModel.getCompany(companyId) {
            fillProfileDetails(it.name, it.bio, it.location.address, it.logo);
        }
    }

    private fun getGroup(groupId: String): Unit {
        studentViewModel.getStudent(groupId) {
            fillProfileDetails(it.name, it.bio, it.institution, it.image);
        }
    }

    private fun handleProfileActionButtons(userId: String) {
        val connectedUserId = userAuthViewModel.getUserId();
        editButton = view.findViewById(R.id.profile_edit_button);
        logoutButton = view.findViewById(R.id.logout_button);

        if (userId === connectedUserId) {
            editButton?.visibility = View.VISIBLE;
            logoutButton?.visibility = View.VISIBLE;

            editButton?.setOnClickListener {
                val args = Bundle()
                args.putString("userId", userId)

                userAuthViewModel.getInfoOnUser(userId) { userInfo, error ->
                    when (userInfo) {
                        is UserInfo.UserStudent -> {
                            Navigation.findNavController(view).navigate(
                                R.id.action_profileFragment_to_editGroupProfileFragment,
                                args
                            )
                        }
                        is UserInfo.UserCompany -> {
                            Navigation.findNavController(view).navigate(
                                R.id.action_profileFragment_to_editCompanyProfileFragment,
                                args
                            )
                        }
                        null -> {
                            // Handle null case if needed
                        }
                    }
                }
            }

            logoutButton?.setOnClickListener {
                userAuthViewModel.logOutUser();
                Navigation.findNavController(it)
                    .navigate(R.id.action_profileFragment_to_startPageFragment)
            }

        } else {
            editButton?.visibility = View.INVISIBLE;
            logoutButton?.visibility = View.INVISIBLE;
        }
    }

    private fun fillProfileDetails(name: String, bio: String, additionalInfo: String, image: String): Unit {
            nameTV = view.findViewById(R.id.profile_name)
            nameTV?.text = name

            bioTV = view.findViewById(R.id.profile_bio)
            bioTV?.text = bio

            additionalInfoTV = view.findViewById(R.id.profile_additional_info)
            additionalInfoTV?.text = additionalInfo;

            profileImage = view.findViewById(R.id.profile_image)
            profileImageBackgroundElement = view.findViewById(R.id.profile_background_image);

            if (image != "DEFAULT LOGO" && profileImage != null && profileImageBackgroundElement != null) {
//                Picasso.get().load(image).into(profileImage)
//                Picasso.get().load(image).into(profileImageBackgroundElement)

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


    private fun fillPosts(userId: String): Unit {
        postsRecyclerView = view.findViewById(R.id.profile_posts_recycler_view)
        noPostsWarningTV = view.findViewById(R.id.profile_no_posts_text)

        postViewModel.getPostsByOwnerId(userId) {
            if (it.isEmpty()) {
                postsRecyclerView.visibility = View.GONE;
                noPostsWarningTV?.visibility = View.VISIBLE;
            } else {
                postsRecyclerView.visibility = View.VISIBLE;
                noPostsWarningTV?.visibility = View.GONE;

                postsRecyclerView.setPadding(0, 0, 0, 250)
                postAdapter.posts = it.toMutableList()
                postsRecyclerView.layoutManager = LinearLayoutManager(context)
                postsRecyclerView.adapter = postAdapter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
