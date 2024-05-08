package com.android.skillsync

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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

    private var loadingOverlay: LinearLayout? = null;

    private var isPostsLoaded: Boolean = false;
    private var isProfileDataLoaded: Boolean = false;

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

        ActionBarHelper.showActionBarAndBottomNavigationView(requireActivity() as? AppCompatActivity)

        val args = arguments
        val userId = args?.getString("userId") ?: userAuthViewModel.getUserId()

        val isFromArgs = args != null
        postAdapter = PostAdapter(mutableListOf(), false, isFromArgs)

        loadingOverlay = view.findViewById(R.id.profile_loading_overlay);
        handleLoading();

        if (userId !== null) {
            userAuthViewModel.getInfoOnUser(userId) { userInfo, error ->
                when (userInfo) {
                    is UserInfo.UserStudent -> {
                        getGroup(userId)
                    }
                    is UserInfo.UserCompany -> {
                        getCompany(userId);
                    }

                    null -> Log.d("UserInfo", "we have identify unknown user")
                }
            }

            handleProfileActionButtons(userId)
            fillPosts(userId)
        }

        if(args?.getString("userId")?.isNotEmpty() == true) {
            backButton = view.findViewById(R.id.back_button)
            backButton?.setVisibility(View.VISIBLE)

            backButton?.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack(
                    "profileBackStack",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            }
        }
        val mapButton = view.findViewById<TextView>(R.id.explore_companies)

        if(args?.getString("userId") != null) {
            mapButton.visibility = View.GONE
        } else {
            mapButton.visibility = View.VISIBLE
            mapButton.setOnClickListener {
                val args = Bundle()
                args.putBoolean("showFeed", false)
                Navigation.findNavController(it)
                    .navigate(R.id.action_profileFragment_to_mapFragment, args)
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
            editButton?.visibility = View.GONE;
            logoutButton?.visibility = View.GONE;
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

            if (profileImage != null && profileImageBackgroundElement != null) {
                val imageUrl = if (image.isEmpty())
                    "https://firebasestorage.googleapis.com/v0/b/skills-e4dc8.appspot.com/o/images%2FuserAvater.png?alt=media&token=1fa189ff-b5df-4b1a-8673-2f8e11638acc"
                else image

                Picasso.get().load(imageUrl).into(profileImage)

                // Blur the background image
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    profileImageBackgroundElement?.setRenderEffect(
                        RenderEffect.createBlurEffect(
                            50f, 50f, Shader.TileMode.CLAMP
                        )
                    )
                }
            }

        isProfileDataLoaded = true;
        handleLoading();
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
                postAdapter.clear()
                postAdapter.addAll(it.toMutableList());
                postsRecyclerView.layoutManager = LinearLayoutManager(context)
                postsRecyclerView.adapter = postAdapter
            }

            isPostsLoaded = true;
            handleLoading();
        }
    }

    private fun handleLoading(): Unit {
        if (isPostsLoaded && isProfileDataLoaded) {
            loadingOverlay?.visibility = View.INVISIBLE;
        } else {
            loadingOverlay?.visibility = View.VISIBLE;
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
