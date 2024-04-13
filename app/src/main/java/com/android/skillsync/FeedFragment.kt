package com.android.skillsync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.skillsync.ViewModel.PostViewModel
import com.android.skillsync.adapters.PostAdapter
import com.android.skillsync.databinding.FragmentFeedBinding
import com.android.skillsync.helpers.ActionBarHelper

class FeedFragment : Fragment() {
    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var viewModel: PostViewModel
    private lateinit var progressBar: ProgressBar

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
        progressBar = binding.progressBar

//        progressBar.visibility = View.VISIBLE

        // Set up RecyclerView
        postsRecyclerView.layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProvider(this)[PostViewModel::class.java]

        swipeRefreshLayout.setOnRefreshListener {
            reloadData()
        }

        // Set up ViewModel
        viewModel.posts.observe(viewLifecycleOwner) { newPosts ->
            // Check if the data has changed, only update the adapter if it's different
            postAdapter = PostAdapter(posts = newPosts)
            postsRecyclerView.adapter = postAdapter

            // Update adapter data
            postAdapter.posts = newPosts
            postAdapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        }

//        viewModel.postsListLoadingState.observe(viewLifecycleOwner) { state ->
//            try {
//                swipeRefreshLayout.isRefreshing = state == PostUseCases.LoadingState.LOADING
//            } catch (e: Exception) {
//                e.printStackTrace() // This will print the error in the logcat
//            }
//        }
        reloadData()

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ActionBarHelper.showActionBarAndBottomNavigationView(requireActivity() as? AppCompatActivity)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun reloadData() {
        progressBar.visibility = View.VISIBLE
        viewModel.refreshPosts()
        progressBar.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}