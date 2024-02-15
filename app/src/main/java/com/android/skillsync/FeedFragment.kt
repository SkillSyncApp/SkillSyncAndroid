package com.android.skillsync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.skillsync.ViewModel.PostViewModel
import com.android.skillsync.adapters.PostAdapter
import com.android.skillsync.databinding.FragmentFeedBinding
import com.android.skillsync.helpers.ActionBarHelper

class FeedFragment : Fragment() {

    private lateinit var postsRecyclerView: RecyclerView;
    private lateinit var postAdapter: PostAdapter;
    private lateinit var viewModel: PostViewModel
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(layoutInflater, container, false)
        view = binding.root
        viewModel = ViewModelProvider(this)[PostViewModel::class.java]
        initPosts()

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ActionBarHelper.showActionBarAndBottomNavigationView(requireActivity() as? AppCompatActivity)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun initPosts() {
        postsRecyclerView = view.findViewById(R.id.postsRecyclerView) ?: return
        postsRecyclerView.setHasFixedSize(true)
        postsRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        postAdapter = PostAdapter(mutableListOf())
        postsRecyclerView.adapter = postAdapter

        postsRecyclerView.setHasFixedSize(true)
        postsRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        viewModel.getPostsLiveData { postsLiveData ->
            postsLiveData.observe(viewLifecycleOwner) { updatedPosts ->
                postAdapter.posts = updatedPosts
                postAdapter.notifyDataSetChanged()
            }
        }
        binding.pullToRefresh.setOnRefreshListener {
            reloadData()
        }
    }

    override fun onResume() {
        super.onResume()
        reloadData()
    }

    private fun reloadData() {
        viewModel.refreshPosts()
    }

//        private fun getPosts(): ArrayList<Post> {
//        posts = ArrayList()
//
//        // TODO: we need to populate the owner details (name + image) from the ownerId
//        val defaultPost1 = Post("111", "Wix", "title", "post content here", "")
//        val defaultPost2 = Post("222", "Amazon", "title", "Amazon's here! this is out new post and work offers. Follow us for more updates and information", "")
//        val defaultPost3 = Post("333", "Fox", "title", "post content here", "")
//        posts.add(defaultPost1)
//        posts.add(defaultPost2)
//        posts.add(defaultPost3)
//
//        return posts
//    }
}