package com.android.skillsync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.skillsync.ViewModel.PostViewModel
import com.android.skillsync.ViewModel.UserAuthViewModel
import com.android.skillsync.adapters.PostAdapter
import com.android.skillsync.databinding.FragmentFeedBinding
import com.android.skillsync.helpers.ActionBarHelper
import com.android.skillsync.models.Post.Post
import com.android.skillsync.models.UserInfo


class FeedFragment : Fragment() {
    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var viewModel: PostViewModel
    private lateinit var progressBar: ProgressBar
    private val userAuthViewModel: UserAuthViewModel by activityViewModels()

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize views
        postsRecyclerView = binding.postsRecyclerView
        swipeRefreshLayout = binding.pullToRefresh
        postAdapter = PostAdapter(mutableListOf())
        viewModel = ViewModelProvider(this)[PostViewModel::class.java]

        postsRecyclerView.setPadding(0, 0, 0, 250)

        postAdapter.posts = viewModel.posts.value?.toMutableList() ?: mutableListOf()
        progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE


        userAuthViewModel.getInfoOnUser(userAuthViewModel.getUserId().toString()) { userInfo, error ->
            when (userInfo) {
                is UserInfo.UserStudent -> {
                    (activity as MainActivity).setProfile("USER")
                }
                is UserInfo.UserCompany -> {
                    (activity as MainActivity).setProfile("COMPANY")
                }

                null -> TODO()
            }
        }


        // Set up RecyclerView
        postsRecyclerView.layoutManager = LinearLayoutManager(context)
        postsRecyclerView.adapter = postAdapter

        swipeRefreshLayout.setOnRefreshListener {
            reloadData()
        }

        // Set up ViewModel
        viewModel.posts.observe(viewLifecycleOwner) { newPosts ->
            // Clear the existing data in the adapter
            if (postAdapter.posts.isNotEmpty()) {
                postAdapter.clear()
            }
            postAdapter.addAll(newPosts)
            postAdapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        }

// next pr
//        postAdapter.setOnPostClickListener(object : OnPostClickListener {
//            override fun onPostClicked(post: Post?) {
//                post?.let { it ->
//                    userAuthViewModel.getInfoOnUser(it.ownerId) { userInfo, error ->
//                        if (error != null) {
//                            Log.e("User Info Error", error)
//                        } else {
//                            val dialogBinding = UserProfileDialogBinding.inflate(layoutInflater)
//                            val dialog = AlertDialog.Builder(requireContext())
//                                .setView(dialogBinding.root)
//                                .create()
//
//                            when (userInfo) {
//                                is UserInfo.UserStudent -> {
//                                    dialogBinding.textViewName.text = userInfo.studentInfo?.name
//                                    dialogBinding.textViewEmail.text = userInfo.studentInfo?.email
//                                    dialogBinding.textViewBio.text = userInfo.studentInfo?.bio
//                                    userInfo.studentInfo?.id?.let { studentId ->
//                                        FeedFragmentDirections.actionFeedFragmentToGroupProfileFragment(userId = studentId)
//                                    }
//                                }
//                                is UserInfo.UserCompany -> {
//                                    dialogBinding.textViewName.text = userInfo.companyInfo?.name
//                                    dialogBinding.textViewEmail.text = userInfo.companyInfo?.email
//                                    dialogBinding.textViewBio.text = userInfo.companyInfo?.bio
//                                    userInfo.companyInfo?.id?.let { companyId ->
//                                        FeedFragmentDirections.actionFeedFragmentToCompanyProfileFragment(userId = companyId)
//                                    }
//                                }
//
//                                null -> throw IllegalArgumentException("unknown userInfo")
//                            }
////
////                            dialogBinding.buttonMoreDetails.setOnClickListener {
////                                when (userInfo) {
////                                    is UserInfo.UserStudent -> {
////                                        userInfo.studentInfo?.id?.let { studentId ->
////                                            val action = FeedFragmentDirections.actionFeedFragmentToGroupProfileFragment(userId = studentId)
////                                            Navigation.findNavController(requireView()).navigate(action)
////                                            dialog.dismiss()
////                                        }
////                                    }
////                                    is UserInfo.UserCompany -> {
////                                        userInfo.companyInfo?.id?.let { companyId ->
////                                            val action = FeedFragmentDirections.actionFeedFragmentToCompanyProfileFragment(userId = companyId)
////                                            Navigation.findNavController(requireView()).navigate(action)
////                                            dialog.dismiss()
////                                        }
////                                    }
////                                }
////                            }
//
////                            dialog.show()
//                        }
//                    }
//                }
//            }
//
//            override fun onPostClick(position: Int) {
//                Log.d("TAG", "postsRecyclerAdapter: post click position $position")
//            }
//        })

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ActionBarHelper.showActionBarAndBottomNavigationView(requireActivity() as? AppCompatActivity)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun reloadData() {
        swipeRefreshLayout.isRefreshing = false
        progressBar.visibility = View.VISIBLE
        viewModel.refreshPosts()
        progressBar.visibility = View.GONE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        reloadData()
    }

    interface OnPostClickListener {
        fun onPostClick(position: Int)
        fun onPostClicked(post: Post?)
    }
}